package com.todolist.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.todolist.dto.ComentarioDto;

@Entity
@Data
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comentario;

    @Column(nullable = false)
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "id_tarea", nullable = false)
    private Tarea tarea;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User autor;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    public ComentarioDto getComentarioDto() {
        ComentarioDto comentarioDto = new ComentarioDto();
        comentarioDto.setIdComentario(this.id_comentario);
        comentarioDto.setContenido(this.contenido);
        comentarioDto.setFechaCreacion(this.fechaCreacion);
        return comentarioDto;
    }
}
