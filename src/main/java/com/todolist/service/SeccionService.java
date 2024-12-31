package com.todolist.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.todolist.model.Seccion;

@Service
public interface SeccionService {
	public ResponseEntity<Map<String, Object>> listarSeccionesPorUserID(Long idUser);

	public ResponseEntity<Map<String, Object>> obtenerSeccionPorID(Long idSeccion);

	public ResponseEntity<Map<String, Object>> registrarSeccion(Seccion seccion);

}
