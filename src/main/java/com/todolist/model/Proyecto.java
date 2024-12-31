package com.todolist.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.todolist.dto.ProyectoDto;

@Entity
@Data
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_proyecto;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "id_color")
    private Color color;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seccion> secciones;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas;
    
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitado> invitados;

    public ProyectoDto getProyectoDto() {
        ProyectoDto proyectoDto = new ProyectoDto();
        proyectoDto.setIdProyecto(id_proyecto);
        proyectoDto.setNombre(nombre);

        if (color != null) {
            proyectoDto.setColor(color.getColorDto()); 
        }
        if (usuario != null) {
            proyectoDto.setUsuario(usuario.getUserDto()); 
         }
        if (secciones != null) {
            proyectoDto.setSecciones(secciones.stream().map(Seccion::getSeccionDto).toList()); 
        }
        if (tareas != null) {
            proyectoDto.setTareas(tareas.stream().map(Tarea::getTareaDto).toList());
        }
        
        if (tareas != null) {
            proyectoDto.setInvitados(invitados.stream().map(Invitado::getInvitadoDto).toList());
        }
        return proyectoDto;
    }
}