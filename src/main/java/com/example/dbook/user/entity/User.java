package com.example.dbook.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "users")
public class User {

    private Long id;

    private String username;
    private String email;
    private String phone;
    private String address;
}
