package com.example.dbook.order.entity;


import lombok.Getter;

@Getter
public enum PlanType {
    MONTHLY("월간 구독권", 10800),
    YEARLY("연간 구독권", 108100);

    private final String description;
    private final int price;

    PlanType(String description, int price){
        this.description = description;
        this.price = price;
    }

    public String getDescription(){return description;}
    public int getPrice(){return price;}
}
