package com.todolist.service;

import com.todolist.dto.SignUpRequest;
import com.todolist.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignUpRequest signUpRequest);
}
