package com.todolist.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.todolist.dto.SeccionDto;
import com.todolist.model.Proyecto;
import com.todolist.model.Seccion;
import com.todolist.model.User;
import com.todolist.repository.ProyectoRepository;
import com.todolist.repository.SeccionRepository;
import com.todolist.repository.UserRepository;

@Service
public class SeccionServiceImpl implements SeccionService {

    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private ProyectoRepository proyectoRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Map<String, Object>> listarSeccionesPorUserID(Long userId) {
        Map<String, Object> respuesta = new HashMap<>();
        List<Seccion> secciones = seccionRepository.findAllByUsuario_Id(userId);
        if (!secciones.isEmpty()) {
        	 List<SeccionDto> seccionDtos = secciones.stream().map(Seccion::getSeccionDto).toList();
            respuesta.put("secciones", seccionDtos);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No existen secciones para este usuario");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> obtenerSeccionPorID(Long idSeccion) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Seccion> seccion = seccionRepository.findById(idSeccion);
        SeccionDto seccionDto = new SeccionDto();
        seccionDto = seccion.get().getSeccionDto();
        if(!seccion.isEmpty()) {
            respuesta.put("mensaje", "Búsqueda correcta");
            respuesta.put("seccion", seccionDto);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + idSeccion);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
    
    
    @Override
    public ResponseEntity<Map<String, Object>> registrarSeccion(Seccion seccion) {
        Map<String, Object> respuesta = new HashMap<>();
        
        Optional<Seccion> seccionExistente = seccionRepository.findByNombreAndProyecto(seccion.getNombre(), seccion.getProyecto());
        if (seccionExistente.isPresent()) {
            respuesta.put("mensaje", "Ya existe una sección con el mismo nombre en este proyecto.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        Optional<Proyecto> proyecto = proyectoRepository.findById(seccion.getProyecto().getId_proyecto());
        if (proyecto.isEmpty()) {
            respuesta.put("mensaje", "El proyecto asociado no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        Optional<User> usuario = userRepository.findById(seccion.getUsuario().getId());
        if (usuario.isEmpty()) {
            respuesta.put("mensaje", "El usuario asociado no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        seccion = seccionRepository.save(seccion);

        SeccionDto resp = new SeccionDto();
        resp.setIdSeccion(seccion.getId_seccion());
        resp.setNombre(seccion.getNombre());

        respuesta.put("mensaje", "Sección creada exitosamente");
        respuesta.put("seccion", resp); 
        respuesta.put("status", HttpStatus.CREATED);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

}
