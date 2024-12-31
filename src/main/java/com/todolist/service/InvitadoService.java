package com.todolist.service;

import java.util.List;

import com.todolist.dto.InvitadoDto;
import com.todolist.dto.UserDto;

public interface InvitadoService {

    InvitadoDto invitarUsuario(Long idProyecto, String user);
    
    List<UserDto> listarUsuariosDelProyecto(Long idProyecto);
}