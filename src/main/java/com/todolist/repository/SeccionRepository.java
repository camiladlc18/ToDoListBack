package com.todolist.repository;

import com.todolist.model.Proyecto;
import com.todolist.model.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findAllByUsuario_Id(Long usuarioId);
    Optional<Seccion> findByNombre(String nombre);
    Optional<Seccion> findByNombreAndProyecto(String nombre, Proyecto proyecto);
}