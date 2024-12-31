package com.todolist.controller;

import com.todolist.dto.InvitadoDto;
import com.todolist.dto.UserDto;
import com.todolist.service.InvitadoService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/invitados")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class InvitadoController {

    @Autowired
    private final InvitadoService invitadoService;

    @PostMapping("/invitar")
    public ResponseEntity<InvitadoDto> invitarUsuario(@RequestParam Long idProyecto, @RequestParam String user) {
        InvitadoDto invitado = invitadoService.invitarUsuario(idProyecto, user);
        return ResponseEntity.ok(invitado);
    }
    
    @GetMapping("/{idProyecto}/usuarios")
    public ResponseEntity<List<UserDto>> listarUsuariosDelProyecto(@PathVariable Long idProyecto) {
        List<UserDto> usuarios = invitadoService.listarUsuariosDelProyecto(idProyecto);
        return ResponseEntity.ok(usuarios);
    }

}
