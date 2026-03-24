package com.example.dbook.payment.controller;

import com.example.dbook.member.entity.Member;
import com.example.dbook.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MemberRepository memberRepository;

    @Value("${payment.client.key}")
    private String CLIENT_KEY;

    @Value("${payment.secret.key}")
    private String  API_SECRET_KEY;

    @GetMapping("/billing")
    public String getBillingPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        String customerKey = "customer_" + member.getId();
        model.addAttribute("clientKey", CLIENT_KEY);
        model.addAttribute("customerKey", member.getId() + "_dbook");
        model.addAttribute("member", member); 

        return "payment/billing";
    }

    @PostMapping("/issue-billing-key")
    @ResponseBody
    @Transactional
    public ResponseEntity<JSONObject> issueBillingKey(@RequestBody String jsonBody, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        JSONObject requestData = parseRequestData(jsonBody);

        JSONObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/authorizations/issue");

        if (!response.containsKey("error")) {
            String billingKey = (String) response.get("billingKey");
            String customerKey = (String) requestData.get("customerKey");

            Member member = memberRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("유저 없음"));

            member.updateBillingKey(billingKey);
            memberRepository.save(member);

            logger.info("성공! customerKey: {}, 발급된 billingKey: {}", customerKey, billingKey);
        }

        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
    }

    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream resStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(resStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            JSONObject err = new JSONObject();
            err.put("error", "연동 실패");
            return err;
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "payment/success";
    }

    @GetMapping("/fail")
    public String failPage() {
        return "payment/fail";
    }


}
