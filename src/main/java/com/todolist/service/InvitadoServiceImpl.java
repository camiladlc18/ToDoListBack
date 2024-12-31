package com.todolist.service;

import com.todolist.dto.InvitadoDto;
import com.todolist.dto.UserDto;
import com.todolist.model.Invitado;
import com.todolist.model.Proyecto;
import com.todolist.model.User;
import com.todolist.repository.InvitadoRepository;
import com.todolist.repository.ProyectoRepository;
import com.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitadoServiceImpl implements InvitadoService {

    @Autowired
    private final InvitadoRepository invitadoRepository;
    @Autowired
    private final ProyectoRepository proyectoRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public InvitadoDto invitarUsuario(Long idProyecto, String user) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no existe"));

        User usuario = userRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        
        if (proyecto.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("No puedes invitarte a ti mismo.");
        }

        Optional<Invitado> existente = invitadoRepository.findByProyectoAndInvitado(proyecto, usuario);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El usuario ya está invitado a este proyecto.");
        }

        Invitado nuevoInvitado = new Invitado();
        nuevoInvitado.setProyecto(proyecto);
        nuevoInvitado.setInvitado(usuario);

        Invitado invitadoGuardado = invitadoRepository.save(nuevoInvitado);
        return invitadoGuardado.getInvitadoDto();
    }
    
    @Override
    public List<UserDto> listarUsuariosDelProyecto(Long idProyecto) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no existe"));

        List<UserDto> usuarios = new ArrayList<>();
        User creador = proyecto.getUsuario();
        usuarios.add(creador.getUserDto());

        List<Invitado> invitados = invitadoRepository.findByProyecto(proyecto);
        for (Invitado invitado : invitados) {
            usuarios.add(invitado.getInvitado().getUserDto());
        }
        return usuarios;
    }
}
