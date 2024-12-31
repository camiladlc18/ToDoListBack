package com.todolist.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String user;
    private String email;
    private String password; 
}
