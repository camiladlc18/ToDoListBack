package com.todolist.controller;

import com.todolist.dto.ColorDto;
import com.todolist.model.Color;
import com.todolist.service.ColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colores")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<Color>> listarColores() {
        List<Color> colores = colorService.listarColores();
        return ResponseEntity.ok(colores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorDto> obtenerColorPorId(@PathVariable Long id) {
        ColorDto color = colorService.obtenerColorPorId(id);
        return ResponseEntity.ok(color);
    }

    @PostMapping
    public ResponseEntity<Color> crearColor(@RequestBody Color color) {
        Color nuevoColor = colorService.crearColor(color);
        return ResponseEntity.ok(nuevoColor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColor(@PathVariable Long id) {
        colorService.eliminarColor(id);
        return ResponseEntity.noContent().build();
    }
}
