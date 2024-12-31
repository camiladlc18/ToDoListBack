package com.todolist.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.todolist.model.Tarea;

@Service
public interface TareaService {
    public ResponseEntity<Map<String, Object>> listarTareasPorUserIDyProyectID(Long idUser, Long idProyecto);

    public ResponseEntity<Map<String, Object>> obtenerTareaPorID(Long idTarea);

    public ResponseEntity<Map<String, Object>> registrarTarea(Tarea tarea);
    
    public ResponseEntity<Map<String, Object>> actualizarTarea(Long idTarea, Tarea tareaActualizada);
    
    public ResponseEntity<Map<String, Object>> eliminarTarea(Long idTarea);
    
    public ResponseEntity<Map<String, Object>> aprobarTarea(Long idTarea);
    
    public ResponseEntity<Map<String, Object>> agregarComentario(Long idTarea, Long idUsuario, String contenido);

    public ResponseEntity<Map<String, Object>> listarComentarios(Long idTarea);
    
    public ResponseEntity<Map<String, Object>> listarTareasPorAsignadoA(String asignadoA);
    
    



}