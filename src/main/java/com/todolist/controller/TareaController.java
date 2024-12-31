package com.todolist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.todolist.model.Tarea;
import com.todolist.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "http://localhost:4200")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping("/usuario/{userId}/{proyectoId}")
    public ResponseEntity<Map<String, Object>> listarTareas(@PathVariable Long userId, @PathVariable Long proyectoId) {
        return tareaService.listarTareasPorUserIDyProyectID(userId, proyectoId);
    }

    @GetMapping("/{idTarea}")
    public ResponseEntity<Map<String, Object>> obtenerTarea(@PathVariable Long idTarea) {
        return tareaService.obtenerTareaPorID(idTarea);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregar(@RequestBody Tarea tarea) {
        return tareaService.registrarTarea(tarea);
    }
   
    @PutMapping("/actualizar/{idTarea}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long idTarea, @RequestBody Tarea tareaActualizada) {
        return tareaService.actualizarTarea(idTarea, tareaActualizada);
    }

    @DeleteMapping("/eliminar/{idTarea}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idTarea) {
        return tareaService.eliminarTarea(idTarea);
    }
    
    @PutMapping("/aprobar/{idTarea}")
    public ResponseEntity<Map<String, Object>> aprobarTarea(@PathVariable Long idTarea) {
        return tareaService.aprobarTarea(idTarea);
    }
    
    @PostMapping("/comentarios/{idTarea}/{idUsuario}")
    public ResponseEntity<Map<String, Object>> agregarComentario(@PathVariable Long idTarea, @PathVariable Long idUsuario, @RequestBody String contenido ) {
        return tareaService.agregarComentario(idTarea, idUsuario, contenido.trim()); 
    }
    
    @GetMapping("/{idTarea}/comentarios")
    public ResponseEntity<Map<String, Object>> listarComentarios(@PathVariable Long idTarea) {
        return tareaService.listarComentarios(idTarea);
    }
    
    @GetMapping("/asignadas/{asignadoA}")
    public ResponseEntity<Map<String, Object>> listarTareasAsignadas(@PathVariable String asignadoA) {
        return tareaService.listarTareasPorAsignadoA(asignadoA);
    }
}
