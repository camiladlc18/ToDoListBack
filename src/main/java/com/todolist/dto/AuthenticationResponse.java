package com.todolist.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private String name;
}
