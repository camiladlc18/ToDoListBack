package com.todolist.repository;

import com.todolist.model.Invitado;
import com.todolist.model.Proyecto;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitadoRepository extends JpaRepository<Invitado,Long> {

    Optional<Invitado> findByProyectoAndInvitado(Proyecto proyecto, User invitado);

    List<Invitado> findByProyecto(Proyecto proyecto);
}
