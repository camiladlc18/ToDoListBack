package com.todolist.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.todolist.dto.TareaDto;
import com.todolist.model.Comentario;
import com.todolist.model.Invitado;
import com.todolist.model.Proyecto;
import com.todolist.model.Tarea;
import com.todolist.model.Tarea.EstadoTarea;
import com.todolist.model.User;
import com.todolist.repository.InvitadoRepository;
import com.todolist.repository.ProyectoRepository;
import com.todolist.repository.TareaRepository;
import com.todolist.repository.UserRepository;

@Service
public class TareaServiceImpl implements TareaService {

    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private ProyectoRepository proyectoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitadoRepository invitadoRepository;
    
    @Override
    public ResponseEntity<Map<String, Object>> listarTareasPorAsignadoA(String asignadoA) {
        Map<String, Object> respuesta = new HashMap<>();
        List<Tarea> tareas = tareaRepository.findByAsignadoAAndActivoTrue(asignadoA);

        if (!tareas.isEmpty()) {
            tareas.sort(Comparator.comparing(Tarea::getPrioridad, 
                Comparator.nullsLast(Comparator.comparingInt(prioridad -> {
                    switch (prioridad) {
                        case ALTA: return 1;
                        case MEDIA: return 2;
                        case BAJA: return 3;
                        default: return Integer.MAX_VALUE;
                    }
                }))
            ).thenComparing(Tarea::getNombre));

            List<TareaDto> tareaDtos = tareas.stream().map(Tarea::getTareaDto).toList();
            respuesta.put("tareas", tareaDtos);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No existen tareas asignadas a este usuario");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> listarTareasPorUserIDyProyectID(Long idUser, Long idProyecto) {
        Map<String, Object> respuesta = new HashMap<>();
        List<Tarea> tareas = tareaRepository.findAllByUsuario_IdAndProyecto_IdProyectoAndActivoTrue(idUser, idProyecto);
        
        if (!tareas.isEmpty()) {
            tareas.removeIf(t -> t.getAsignadoA() == null || t.getAsignadoA().isEmpty());

            tareas.sort(Comparator.comparing(Tarea::getPrioridad, 
                Comparator.nullsLast(Comparator.comparingInt(prioridad -> {
                    switch (prioridad) {
                        case ALTA: return 1;
                        case MEDIA: return 2;
                        case BAJA: return 3;
                        default: return Integer.MAX_VALUE; 
                    }
                }))
            ).thenComparing(Tarea::getNombre)); 

            List<TareaDto> tareaDtos = tareas.stream().map(Tarea::getTareaDto).toList();
            respuesta.put("tareas", tareaDtos);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No existen tareas activas para este usuario y proyecto");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }



    @Override
    public ResponseEntity<Map<String, Object>> obtenerTareaPorID(Long idTarea) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Tarea> tarea = tareaRepository.findById(idTarea);
        TareaDto tareaDto = new TareaDto();
        if(tarea.isPresent()) {
            tareaDto = tarea.get().getTareaDto();
            respuesta.put("mensaje", "Búsqueda correcta");
            respuesta.put("tarea", tareaDto);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + idTarea);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
    

    @Override
    public ResponseEntity<Map<String, Object>> registrarTarea(Tarea tarea) {
        Map<String, Object> respuesta = new HashMap<>();

        Optional<Tarea> tareaExistente = tareaRepository.findByNombreAndProyectoAndActivoTrue(tarea.getNombre(), tarea.getProyecto());
        if (tareaExistente.isPresent()) {
            respuesta.put("mensaje", "Ya existe una tarea con el mismo nombre en este proyecto.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        Optional<Proyecto> proyecto = proyectoRepository.findById(tarea.getProyecto().getId_proyecto());
        if (proyecto.isEmpty()) {
            respuesta.put("mensaje", "El proyecto asociado no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        if (proyecto.get().getUsuario().getId().equals(tarea.getUsuario().getId())) {

        } else {
            Optional<Invitado> invitado = invitadoRepository.findByProyectoAndInvitado(proyecto.get(), tarea.getUsuario());
            if (invitado.isEmpty()) {
                respuesta.put("mensaje", "El usuario no está invitado a este proyecto");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
        }

        if (tarea.getEstado() == null) {
            tarea.setEstado(EstadoTarea.PENDIENTE);
        }

        Tarea tareaGuardada = tareaRepository.save(tarea);

        TareaDto resp = new TareaDto();
        resp.setIdTarea(tareaGuardada.getIdTarea());
        resp.setNombre(tareaGuardada.getNombre());
        resp.setDescripcion(tareaGuardada.getDescripcion());
        resp.setPrioridad(tareaGuardada.getPrioridad() != null ? tareaGuardada.getPrioridad().name() : null);
        resp.setFechaVencimiento(tareaGuardada.getFechaVencimiento());
        resp.setEstado(tareaGuardada.getEstado() != null ? tareaGuardada.getEstado().name() : null);
        resp.setFechaCreacion(tareaGuardada.getFechaCreacion());
        resp.setFechaActualizacion(tareaGuardada.getFechaActualizacion());

        respuesta.put("mensaje", "Tarea creada exitosamente");
        respuesta.put("tarea", resp);
        respuesta.put("status", HttpStatus.CREATED);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }



    @Override
    public ResponseEntity<Map<String, Object>> actualizarTarea(Long idTarea, Tarea tareaActualizada) {
        Map<String, Object> respuesta = new HashMap<>();

        Optional<Tarea> tareaExistente = tareaRepository.findById(idTarea);
        if (tareaExistente.isEmpty()) {
            respuesta.put("mensaje", "Tarea no encontrada con ID: " + idTarea);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Tarea tarea = tareaExistente.get();

        Optional<Tarea> tareaConMismoNombre = tareaRepository.findByNombreAndProyectoAndActivoTrue(
                tareaActualizada.getNombre(), tarea.getProyecto());
        if (tareaConMismoNombre.isPresent() && !tareaConMismoNombre.get().getIdTarea().equals(idTarea)) {
            respuesta.put("mensaje", "Ya existe una tarea con el mismo nombre en este proyecto.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        // Actualización de los campos de la tarea
        tarea.setNombre(tareaActualizada.getNombre());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setPrioridad(tareaActualizada.getPrioridad());
        tarea.setFechaVencimiento(tareaActualizada.getFechaVencimiento());

        // Si el campo `asignadoA` ha cambiado, lo actualizamos también
        if (tareaActualizada.getAsignadoA() != null && !tareaActualizada.getAsignadoA().equals(tarea.getAsignadoA())) {
            tarea.setAsignadoA(tareaActualizada.getAsignadoA());  // Aquí puedes agregar validación si es necesario
        }

        tareaRepository.save(tarea);

        TareaDto tareaDto = tarea.getTareaDto();

        respuesta.put("mensaje", "Tarea actualizada exitosamente");
        respuesta.put("tarea", tareaDto);
        respuesta.put("status", HttpStatus.OK);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> eliminarTarea(Long idTarea) {
        Map<String, Object> respuesta = new HashMap<>();

        Optional<Tarea> tarea = tareaRepository.findByIdTareaAndActivoTrue(idTarea);
        if (tarea.isEmpty()) {
            respuesta.put("mensaje", "Tarea no encontrada con ID: " + idTarea);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Tarea tareaEliminar = tarea.get();
        tareaEliminar.setActivo(false);  // Marcar como eliminada lógicamente
        tareaRepository.save(tareaEliminar);

        respuesta.put("mensaje", "Tarea eliminada lógicamente");
        respuesta.put("status", HttpStatus.OK);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    
    @Override
    public ResponseEntity<Map<String, Object>> aprobarTarea(Long idTarea) {
        Map<String, Object> respuesta = new HashMap<>();

        Optional<Tarea> tareaOpt = tareaRepository.findById(idTarea);
        if (tareaOpt.isEmpty()) {
            respuesta.put("mensaje", "Tarea no encontrada con ID: " + idTarea);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Tarea tarea = tareaOpt.get();
        
        if (tarea.getEstado() == EstadoTarea.DESARROLLADA) {
            respuesta.put("mensaje", "La tarea ya está marcada como desarrollada.");
            respuesta.put("status", HttpStatus.BAD_REQUEST);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
        
        tarea.setEstado(EstadoTarea.DESARROLLADA);
        
        tarea.setFechaActualizacion(LocalDateTime.now());
        
        tareaRepository.save(tarea);  

        respuesta.put("mensaje", "Tarea aprobada como desarrollada");
        respuesta.put("tarea", tarea.getTareaDto());  
        respuesta.put("status", HttpStatus.OK);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> agregarComentario(Long idTarea, Long idUsuario, String contenido) {
        Map<String, Object> respuesta = new HashMap<>();

        Optional<Tarea> tareaOptional = tareaRepository.findByIdTareaAndActivoTrue(idTarea);
        if (tareaOptional.isEmpty()) {
            respuesta.put("mensaje", "La tarea no existe o no está activa");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        Tarea tarea = tareaOptional.get();

        Optional<User> usuarioOptional = userRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            respuesta.put("mensaje", "El usuario no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        User usuario = usuarioOptional.get();

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setTarea(tarea);
        comentario.setAutor(usuario);
        comentario.setFechaCreacion(LocalDateTime.now());
        
        tarea.getComentarios().add(comentario); 
        tareaRepository.save(tarea); 

        respuesta.put("mensaje", "Comentario agregado exitosamente");
        respuesta.put("comentario", comentario.getComentarioDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
    
    
    @Override
    public ResponseEntity<Map<String, Object>> listarComentarios(Long idTarea) {
        Map<String, Object> respuesta = new HashMap<>();

        // Buscar la tarea activa por ID
        Optional<Tarea> tareaOptional = tareaRepository.findByIdTareaAndActivoTrue(idTarea);
        if (tareaOptional.isEmpty()) {
            respuesta.put("mensaje", "La tarea no existe o no está activa");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        Tarea tarea = tareaOptional.get();

        // Obtener los comentarios de la tarea
        List<Comentario> comentarios = tarea.getComentarios();
        if (comentarios.isEmpty()) {
            respuesta.put("mensaje", "No hay comentarios para esta tarea");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        }

        // Convertir los comentarios a DTOs
        List<Map<String, Object>> comentariosDto = comentarios.stream()
            .map(comentario -> {
                Map<String, Object> comentarioDto = new HashMap<>();
                comentarioDto.put("contenido", comentario.getContenido());
                comentarioDto.put("autor", comentario.getAutor().getNombre());
                comentarioDto.put("fechaCreacion", comentario.getFechaCreacion());
                return comentarioDto;
            })
            .toList();

        // Crear respuesta
        respuesta.put("mensaje", "Comentarios listados exitosamente");
        respuesta.put("comentarios", comentariosDto);
        respuesta.put("status", HttpStatus.OK);
        respuesta.put("fecha", new Date());

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

}

