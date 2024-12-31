package com.todolist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import com.todolist.dto.SeccionDto;
import com.todolist.dto.TareaDto;

@Entity
@Data
public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seccion;

    @Column(nullable = false)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @OneToMany(mappedBy = "seccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas;
    
    public SeccionDto getSeccionDto() {
        SeccionDto seccionDto = new SeccionDto();
        seccionDto.setIdSeccion(id_seccion);
        seccionDto.setNombre(nombre);
        List<TareaDto> tareaDtos = tareas.stream()
                .map(Tarea::getTareaDto)  
                .collect(Collectors.toList());
seccionDto.setTareas(tareaDtos);
        return seccionDto;
    }
}
