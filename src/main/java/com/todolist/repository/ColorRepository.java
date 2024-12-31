package com.todolist.repository;

import com.todolist.model.Color;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByNombre(String nombre);
}
