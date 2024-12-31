package com.todolist.dto;

import java.util.List;
import lombok.Data;

@Data
public class SeccionDto {
    private Long idSeccion;
    private String nombre;
    private List<TareaDto> tareas;
}