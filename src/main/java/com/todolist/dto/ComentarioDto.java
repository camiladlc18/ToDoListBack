package com.todolist.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ComentarioDto {
    private Long idComentario;
    private String contenido;
    private LocalDateTime fechaCreacion;
}
