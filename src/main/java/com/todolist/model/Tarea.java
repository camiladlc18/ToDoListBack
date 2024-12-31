package com.todolist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.todolist.dto.TareaDto;



@Entity
@Data
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarea;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    
    @Column(name = "asignado_a")
    private String asignadoA;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;

    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = true)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_seccion", nullable = true)
    private Seccion seccion;
    
    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    @Column(nullable = false)
    private boolean activo = true;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        estado = EstadoTarea.PENDIENTE; 
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum Prioridad {
        ALTA,
        MEDIA,
        BAJA
    }

    public enum EstadoTarea {
        PENDIENTE,
        DESARROLLADA
    }
    
    public TareaDto getTareaDto() {
        TareaDto tareaDto = new TareaDto();
        tareaDto.setIdTarea(idTarea);
        tareaDto.setNombre(nombre);
        tareaDto.setDescripcion(descripcion);
        tareaDto.setPrioridad(prioridad != null ? prioridad.name() : null);
        tareaDto.setFechaVencimiento(fechaVencimiento);
        tareaDto.setEstado(estado != null ? estado.name() : null);
        if (comentarios != null) {
        	tareaDto.setComentario(comentarios.stream().map(Comentario::getComentarioDto).toList());
        }
        tareaDto.setFechaCreacion(fechaCreacion);
        tareaDto.setFechaActualizacion(fechaActualizacion);
        tareaDto.setAsignadoA(asignadoA);
        return tareaDto;
    }

}
