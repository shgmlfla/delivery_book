package com.example.dbook.payment.service;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import com.example.dbook.order.entity.Orders;
import com.example.dbook.order.entity.PlanType;
import com.example.dbook.order.entity.Subscription;
import com.example.dbook.order.repository.OrderRepository;
import com.example.dbook.order.service.SubscriptionService;
import com.example.dbook.payment.entity.Payment;
import com.example.dbook.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;

    @Value("${payment.secret.key}")
    private String secretKey;


    @Transactional
    public void processSubscriptionPayment(Member member, String planName, Integer price, PlanType planType) throws Exception{

        String tossOrderId = "ORD_" + member.getId() + "_" + System.currentTimeMillis();

        Orders order = Orders.builder()
                .member(member)
                .tossOrderId(tossOrderId)
                .orderDate(LocalDateTime.now())
                .total_price(price)
                .orderStatus(Orders.OrderStatus.READY)
                .orderType(Orders.OrderType.SUBSCRIPTION)
                .build();
        orderRepository.save(order);

        //토스 api 호출
        JSONObject requestData = new JSONObject();
        requestData.put("amount", price);
        requestData.put("orderId", tossOrderId);
        requestData.put("orderName", planName + " 정기 구독");
        requestData.put("customerKey", "customer_" + member.getId());

        String url = "https://api.tosspayments.com/v1/billing/" + member.getBillingKey();
        JSONObject response = sendRequest(requestData, secretKey, url);

        if (response.containsKey("paymentKey") && "DONE".equals(response.get("status"))) {
            completePayment(order, member, response, planType);
        } else {
            failPayment(order, response);
        }
    }

    private void completePayment(Orders order, Member member, JSONObject response, PlanType planType){
        String approvedAtStr = (String) response.get("approvedAt");
        LocalDateTime approvedAt = OffsetDateTime.parse(approvedAtStr).toLocalDateTime();

        Payment payment = Payment.builder()
                .tossPaymentKey((String) response.get("paymentKey"))
                .tossOrderId((String) response.get("orderId"))
                .amount(Long.parseLong(response.get("totalAmount").toString()))
                .paymentMethod((String) response.get("method"))
                .approvedAt(approvedAt)
                .order(order)
                .member(member)
                .build();

        paymentRepository.save(payment);

        order.setOrderStatus(Orders.OrderStatus.PAYMENT_COMPLETED);

        subscriptionService.createOrUpadateSubscription(member, planType);

        log.info("결제 성공");
    }

    private void failPayment(Orders order, JSONObject response) {
        String errorCode = (String) response.get("code");
        String errorMessage = (String) response.get("message");

        order.setOrderStatus(Orders.OrderStatus.PAYMENT_FAILED);

        log.error("결제 실패");
    }

    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            log.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }

    }
    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}
