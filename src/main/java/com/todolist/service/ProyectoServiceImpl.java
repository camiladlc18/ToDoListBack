package com.todolist.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.todolist.dto.ProyectoDto;
import com.todolist.model.Color;
import com.todolist.model.Proyecto;
import com.todolist.model.User;
import com.todolist.repository.ColorRepository;
import com.todolist.repository.ProyectoRepository;
import com.todolist.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService{
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	@Autowired
	private ColorRepository colorRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ResponseEntity<Map<String, Object>> listarProyectoPorUserID(Long userId) {
	    Map<String, Object> respuesta = new HashMap<>();

	    List<Proyecto> proyectosPropietario = proyectoRepository.findAllByUsuario_Id(userId);    
	    List<Proyecto> proyectosInvitado = proyectoRepository.findProyectosByInvitado(userId);  
	    
	    Set<Proyecto> proyectosUnicos = new HashSet<>(proyectosPropietario);
	    proyectosUnicos.addAll(proyectosInvitado);
	    
	    if (!proyectosUnicos.isEmpty()) {
	        List<Proyecto> listaProyectosOrdenados = new ArrayList<>(proyectosUnicos);
	        listaProyectosOrdenados.sort(Comparator.comparing(Proyecto::getNombre)); 

	        List<ProyectoDto> proyectoDtos = listaProyectosOrdenados.stream()
	                .map(Proyecto::getProyectoDto)
	                .collect(Collectors.toList());
	        
	        respuesta.put("proyectos", proyectoDtos);
	        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	    } else {
	        respuesta.put("mensaje", "No existen proyectos para este usuario");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
	    }
	}



	@Override
	public ResponseEntity<Map<String, Object>> obtenerProyectoPorID(Long idProyecto) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);
		ProyectoDto proyectoDto = new ProyectoDto();
		proyectoDto = proyecto.get().getProyectoDto();
		if(!proyecto.isEmpty()) {
			respuesta.put("mensaje", "Busqueda correcta");
			respuesta.put("proyecto", proyectoDto);
			respuesta.put("status", HttpStatus.OK);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		}else {
			respuesta.put("mensaje", "Sin registros con ID: " + idProyecto);
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> registrarProyecto(Proyecto proyecto) {
	    Map<String, Object> respuesta = new HashMap<>();

	    Optional<Proyecto> proyectoExistente = proyectoRepository.findByNombreAndUsuario(proyecto.getNombre(), proyecto.getUsuario());
	    if (proyectoExistente.isPresent()) {
	        respuesta.put("mensaje", "Ya existe un proyecto con el mismo nombre.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
	    }

	    Color color = colorRepository.findById(proyecto.getColor().getIdColor()).orElse(null);
	    User usuario = userRepository.findById(proyecto.getUsuario().getId()).orElse(null);

	    if (color == null || usuario == null) {
	        respuesta.put("mensaje", "Color o Usuario no encontrados.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
	    }

	    proyecto.setColor(color);
	    proyecto.setUsuario(usuario);

	    proyectoRepository.save(proyecto);

	    Optional<Proyecto> bodyProyecto = proyectoRepository.findById(proyecto.getId_proyecto());
	    ProyectoDto resp = bodyProyecto.map(Proyecto::getProyectoDto).orElse(new ProyectoDto());

	    respuesta.put("mensaje", "Proyecto creado exitosamente");
	    respuesta.put("proyecto", resp);
	    respuesta.put("status", HttpStatus.CREATED);
	    respuesta.put("fecha", new Date());

	    return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}
	
	
	@Override
	public ResponseEntity<Map<String, Object>> actualizarProyecto(Long idProyecto, Proyecto proyectoActualizado) {
	    Map<String, Object> respuesta = new HashMap<>();
	    
	    Optional<Proyecto> proyectoExistente = proyectoRepository.findById(idProyecto);
	    if (proyectoExistente.isEmpty()) {
	        respuesta.put("mensaje", "Proyecto no encontrado con ID: " + idProyecto);
	        respuesta.put("status", HttpStatus.NOT_FOUND);
	        respuesta.put("fecha", new Date());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
	    }
	    
	    Proyecto proyecto = proyectoExistente.get();

	    Optional<Proyecto> proyectoConMismoNombre = proyectoRepository.findByNombreAndUsuario(
	            proyectoActualizado.getNombre(), proyecto.getUsuario());
	    if (proyectoConMismoNombre.isPresent() && !proyectoConMismoNombre.get().getId_proyecto().equals(idProyecto)) {
	        respuesta.put("mensaje", "Ya existe un proyecto con el mismo nombre.");
	        respuesta.put("status", HttpStatus.BAD_REQUEST);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
	    }

	    proyecto.setNombre(proyectoActualizado.getNombre());

	    if (proyectoActualizado.getColor() != null) {
	        Color color = colorRepository.findById(proyectoActualizado.getColor().getIdColor()).orElse(null);
	        if (color == null) {
	            respuesta.put("mensaje", "Color no encontrado.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
	        }
	        proyecto.setColor(color);
	    }

	    proyectoRepository.save(proyecto);
	    ProyectoDto proyectoDto = proyecto.getProyectoDto();

	    respuesta.put("mensaje", "Proyecto actualizado exitosamente");
	    respuesta.put("proyecto", proyectoDto);
	    respuesta.put("status", HttpStatus.OK);
	    respuesta.put("fecha", new Date());

	    return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> eliminarProyecto(Long idProyecto) {
	    Map<String, Object> respuesta = new HashMap<>();
	    
	    Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);
	    if (proyecto.isEmpty()) {
	        respuesta.put("mensaje", "Proyecto no encontrado con ID: " + idProyecto);
	        respuesta.put("status", HttpStatus.NOT_FOUND);
	        respuesta.put("fecha", new Date());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
	    }
	    
	    proyectoRepository.delete(proyecto.get());

	    respuesta.put("mensaje", "Proyecto eliminado exitosamente");
	    respuesta.put("status", HttpStatus.OK);
	    respuesta.put("fecha", new Date());

	    return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	}
}
