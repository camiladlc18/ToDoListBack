package com.todolist.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.todolist.model.User;

public interface UserService {
	UserDetailsService userDetailsService();

	User obtenerUsuarioPorId(Long userId);
}
