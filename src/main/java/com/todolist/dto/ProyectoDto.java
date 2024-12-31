package com.todolist.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProyectoDto {

    private Long idProyecto;
    private String nombre;
    private ColorDto color;
    private UserDto usuario;
    private List<SeccionDto> secciones;
    private List<TareaDto> tareas;
    private List<InvitadoDto> invitados;
}