package com.examen2.GestionCRUD.repository;

import com.examen2.GestionCRUD.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    long countByCategoria(String categoria);
    Optional<Producto> findTopByCategoriaOrderByIdDesc(String categoria);
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE productos", nativeQuery = true)
    void vaciarTablaYReiniciarIds();
}