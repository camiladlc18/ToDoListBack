package com.todolist.repository;

import com.todolist.model.Proyecto;
import com.todolist.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
	@Query("SELECT t FROM Tarea t WHERE t.usuario.id = :usuarioId AND t.proyecto.id_proyecto = :proyectoId AND t.activo = true")
	List<Tarea> findAllByUsuario_IdAndProyecto_IdProyectoAndActivoTrue(
	    @Param("usuarioId") Long usuarioId, 
	    @Param("proyectoId") Long proyectoId
	);

    Optional<Tarea> findByIdTareaAndActivoTrue(Long id);
    
    @Query("SELECT t FROM Tarea t WHERE t.nombre = :nombre AND t.proyecto = :proyecto AND t.activo = true")
    Optional<Tarea> findByNombreAndProyectoAndActivoTrue(
        @Param("nombre") String nombre, 
        @Param("proyecto") Proyecto proyecto
    );
    
    @Query("SELECT t FROM Tarea t WHERE t.asignadoA = :asignadoA AND t.activo = true")
    List<Tarea> findByAsignadoAAndActivoTrue(@Param("asignadoA") String asignadoA);

}

