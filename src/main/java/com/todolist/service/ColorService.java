package com.todolist.service;

import com.todolist.dto.ColorDto;
import com.todolist.model.Color;
import com.todolist.repository.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public List<Color> listarColores() {
        return colorRepository.findAll();
    }

    public ColorDto obtenerColorPorId(Long id) {
    	Color color = colorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No se encontró un color con el ID proporcionado: " + id));
        ColorDto colorDto = new ColorDto();
        colorDto.setIdColor(color.getIdColor());
        colorDto.setNombre(color.getNombre());
        colorDto.setCodigoHex(color.getCodigoHex());
        
        return colorDto;
    }

    public Color crearColor(Color color) {
        return colorRepository.save(color);
    }


    public void eliminarColor(Long id) {
        if (!colorRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. El color con ID: " + id + " no existe.");
        }
        colorRepository.deleteById(id);
    }
}
