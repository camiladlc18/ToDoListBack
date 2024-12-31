package com.todolist.service;

import org.springframework.http.ResponseEntity;
import com.todolist.model.Proyecto;
import java.util.Map;

public interface ProyectoService {

	public ResponseEntity<Map<String, Object>> listarProyectoPorUserID(Long idUser);
	
	public ResponseEntity<Map<String, Object>> obtenerProyectoPorID(Long idProyecto);
	
	public ResponseEntity<Map<String, Object>> registrarProyecto(Proyecto proyecto);
	
	public ResponseEntity<Map<String, Object>> actualizarProyecto(Long idProyecto, Proyecto proyectoActualizado);
	
	public ResponseEntity<Map<String, Object>> eliminarProyecto(Long idProyecto);
}
