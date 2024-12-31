package com.todolist.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.todolist.model.Proyecto;
import com.todolist.service.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;
    
    @GetMapping("/usuario/{idUser}")
    public ResponseEntity<Map<String, Object>> listarProyectos(@PathVariable Long idUser) {
        return proyectoService.listarProyectoPorUserID(idUser);
    }
    
    @GetMapping("/{idProyecto}")
    public ResponseEntity<Map<String, Object>> obtenerProyecto(@PathVariable Long idProyecto) {
        return proyectoService.obtenerProyectoPorID(idProyecto);
    }
    
    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregar(@RequestBody Proyecto proyecto) {
        return proyectoService.registrarProyecto(proyecto);
    }
    
    @PutMapping("/actualizar/{idProyecto}")
    public ResponseEntity<Map<String, Object>> actualizar( @PathVariable Long idProyecto,  @RequestBody Proyecto proyectoActualizado) {
        return proyectoService.actualizarProyecto(idProyecto, proyectoActualizado);
    }

    @DeleteMapping("/eliminar/{idProyecto}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idProyecto) {
        return proyectoService.eliminarProyecto(idProyecto);
    }
}