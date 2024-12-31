package com.todolist.repository;

import com.todolist.model.Proyecto;
import com.todolist.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
	
    List<Proyecto> findAllByUsuario_Id(Long usuarioId);
    
    Optional<Proyecto> findByNombreAndUsuario(String nombre, User usuario);
    
    List<Proyecto> findByUsuario(User usuario);

    
    @Query("SELECT i.proyecto FROM Invitado i WHERE i.invitado.id = :idUsuario")
    List<Proyecto> findProyectosByInvitado(@Param("idUsuario") Long idUsuario);
}
