package com.todolist.model;

import com.todolist.dto.InvitadoDto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Invitado {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_invitado;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User invitado;
    
    public InvitadoDto getInvitadoDto() {
        InvitadoDto invitadoDto = new InvitadoDto();
        invitadoDto.setIdInvitado(this.id_invitado);

        if (this.invitado != null) {
            invitadoDto.setUsuario(this.invitado.getUserDto());
        }

        return invitadoDto;
    }
}
