package com.app.messagequeue.dto;

public record AccountMessageDto(Long accountNumber, String name, String email,
    String mobileNumber) {
}
