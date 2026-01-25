package com.examen2.GestionCRUD.controller;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok("Producto eliminado correctamente ");
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        productoService.eliminarTodos();
        return ResponseEntity.noContent().build();
    }

    // Endpoint oculto para cargar 60 datos masivos manualmente
    // URL: http://localhost:8080/demoJPA/products/cargar-datos
    @PostMapping("/cargar-datos") 
    public ResponseEntity<String> cargarDatosPrueba() {
        
        // Creamos un bucle que se repita 60 veces
        for (int i = 1; i <= 60; i++) {
            ProductoDTO p = new ProductoDTO();
            
            // Usamos lógica matemática simple para variar los datos
            p.setName("Producto Generado " + i);
            
            // SKUs únicos usando el tiempo del sistema para evitar errores de duplicados
            p.setSku("GEN-" + i + "-" + System.currentTimeMillis()); 
            
            p.setPrice(10.0 + (i * 2)); // El precio sube de 2 en 2
            p.setStock(50 + i);         // El stock varía
            
            // Alternamos categorías: Pares = Tecnología, Impares = Hogar
            p.setCategory(i % 2 == 0 ? "Tecnología" : "Hogar");
            
            p.setDescription("Descripción automática para el item número " + i);

            // Guardamos cada uno en la base de datos
            productoService.guardar(p);
        }

        return ResponseEntity.ok("Éxito! Se han agregado 60 productos nuevos al inventario");
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

