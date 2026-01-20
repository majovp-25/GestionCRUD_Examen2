package com.examen2.GestionCRUD.repository;

import com.examen2.GestionCRUD.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Al extender de JpaRepository, Spring Boot ya nos regala:
    // .save(), .findAll(), .findById(), .deleteById()
}