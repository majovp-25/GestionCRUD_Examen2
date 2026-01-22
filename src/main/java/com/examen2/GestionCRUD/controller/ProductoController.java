package com.examen2.GestionCRUD.controller;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demoJPA/products")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    // GET /demoJPA/products
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    // GET /demoJPA/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    // POST /demoJPA/products
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO productoDTO) {
        validarRequest(productoDTO);
        ProductoDTO creado = productoService.guardar(productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PATCH /demoJPA/products/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarParcial(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        validarRequest(productoDTO);
        return ResponseEntity.ok(productoService.actualizar(id, productoDTO));
    }

    // PUT /demoJPA/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarCompleto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        validarRequest(productoDTO);
        return ResponseEntity.ok(productoService.actualizar(id, productoDTO));
    }

    // DELETE /demoJPA/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        productoService.eliminarTodos();
        return ResponseEntity.noContent().build();
    }

    /**
     * Validación mínima para evitar guardar registros vacíos.
     */
    private void validarRequest(ProductoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El cuerpo de la petición es obligatorio");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (dto.getPrice() == null || dto.getPrice() <= 0) {
        throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser >= 0");
        }
        if (dto.getCategory() == null || dto.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
    }
}

