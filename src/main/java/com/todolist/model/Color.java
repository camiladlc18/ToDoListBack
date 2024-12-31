package com.todolist.model;

import com.todolist.dto.ColorDto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false, length = 7)
    private String codigoHex;
    
    public ColorDto getColorDto() {
        ColorDto colorDto = new ColorDto();
        colorDto.setIdColor(this.idColor);
        colorDto.setNombre(this.nombre);
        colorDto.setCodigoHex(this.codigoHex);
        return colorDto;
    }
}