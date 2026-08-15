package com.example.demo.model;

public record RegisterRequest(
    String email, String password, Role role, String name, String firstName, String ref) {}
