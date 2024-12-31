package com.todolist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.todolist.model.Seccion;
import com.todolist.service.SeccionService;

@RestController
@RequestMapping("/api/secciones")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class SeccionController {

    @Autowired
    private SeccionService seccionService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<Map<String, Object>> listarSecciones(@PathVariable Long userId) {
        return seccionService.listarSeccionesPorUserID(userId);
    }

    @GetMapping("/{idSeccion}")
    public ResponseEntity<Map<String, Object>> obtenerSeccion(@PathVariable Long idSeccion) {
        return seccionService.obtenerSeccionPorID(idSeccion);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregar(@RequestBody Seccion seccion) {
        return seccionService.registrarSeccion(seccion);
    }
}
