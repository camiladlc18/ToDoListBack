package com.todolist.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TareaDto {
	private Long idTarea;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaVencimiento;
    private String estado;  
    private String asignadoA; 
    private List<ComentarioDto> comentario;
    private LocalDateTime fechaCreacion;  
    private LocalDateTime fechaActualizacion; 
    
}
