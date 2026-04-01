package com.example.dbook.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @GetMapping("/plans")
    public String showPlans(){
        return "order/subscriptionPlan";
    }
}
