package com.examen2.GestionCRUD.controller;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.List;

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
    //Endpoint oculto para eliminar todos los productos
    // URL: http://localhost:8080/demoJPA/products (DELETE)
    @DeleteMapping
    public ResponseEntity<Void> eliminarTodos() {
        productoService.eliminarTodos();
        return ResponseEntity.noContent().build();
    }

    // Endpoint para cargar 60 datos (15 reales + 45 relleno)
    // URL: http://localhost:8080/demoJPA/products/cargar-datos
    @PostMapping("/cargar-datos")
    public ResponseEntity<String> cargarDatosReales() {
        
        // 1. LISTA DE PRODUCTOS REALES (Tus datos originales)
        List<ProductoDTO> destacados = Arrays.asList(
            // TECNOLOGÍA
            crearProducto("Laptop Gamer Dell", "TEC-DELL-001", "Ryzen 7, 16GB RAM, RTX 3060", 1200.00, 5, "Tecnología"),
            crearProducto("iPhone 15 Pro", "TEC-IPH-002", "Titanio, 256GB", 1100.00, 8, "Tecnología"),
            crearProducto("Monitor LG Ultrawide", "TEC-MON-003", "34 pulgadas, 144Hz", 450.00, 3, "Tecnología"),
            crearProducto("Teclado Keychron K2", "TEC-KEY-004", "Mecánico, Inalámbrico", 95.00, 15, "Tecnología"),
            crearProducto("Mouse MX Master 3", "TEC-MOU-005", "Ergonómico, Productividad", 80.00, 20, "Tecnología"),
            
            // HOGAR
            crearProducto("Cafetera Italiana", "HOG-CAF-006", "Acero inoxidable, 6 tazas", 25.00, 12, "Hogar"),
            crearProducto("Juego de Sábanas", "HOG-SAB-007", "Algodón 500 hilos, Queen", 60.00, 10, "Hogar"),
            crearProducto("Aspiradora Robot", "HOG-ASP-008", "Mapeo inteligente, Wifi", 250.00, 4, "Hogar"),
            
            // ROPA
            crearProducto("Chaqueta de Cuero", "ROP-CHA-009", "Estilo Biker, Negra", 85.00, 6, "Ropa"),
            crearProducto("Zapatillas Running", "ROP-ZAP-010", "Amortiguación Gel, Talla 42", 70.00, 14, "Ropa"),
            crearProducto("Camisa de Lino", "ROP-CAM-011", "Color beige, corte Slim", 35.00, 18, "Ropa"),
            
            // ACCESORIOS Y DEPORTES
            crearProducto("Mochila Antirrobo", "ACC-MOC-012", "Impermeable con USB", 55.00, 22, "Accesorios"),
            crearProducto("Reloj Casio Vintage", "ACC-REL-013", "Digital, Plateado", 45.00, 25, "Accesorios"),
            crearProducto("Balón de Fútbol", "DEP-BAL-014", "Nº 5, Profesional", 30.00, 40, "Deportes"),
            crearProducto("Mat de Yoga", "DEP-YOG-015", "Antideslizante, 6mm", 20.00, 15, "Deportes")
        );

        // Guardamos los destacados
        for (ProductoDTO p : destacados) {
            productoService.guardar(p);
        }

        // 3. RELLENO MASIVO (Hasta llegar a 60)
        String[] categorias = {"Tecnología", "Hogar", "Ropa", "Accesorios", "Deportes", "Juguetes"};
        
        for (int i = 1; i <= 45; i++) {
            String cat = categorias[i % categorias.length];
            String nombre = "Producto " + cat + " " + i;
            String sku = "GEN-" + cat.substring(0, 3).toUpperCase() + "-" + (100 + i); // Genera SKU único
            
            ProductoDTO p = crearProducto(
                nombre, 
                sku, 
                "Descripción generada automáticamente para el ítem #" + i,
                10.00 + (i * 1.5), 
                (int)(Math.random() * 50) + 1, 
                cat
            );
            
            productoService.guardar(p);
        }

        return ResponseEntity.ok("Base de datos recargada");
    }

    // Método auxiliar para crear DTOs rápido y limpio
    private ProductoDTO crearProducto(String nombre, String sku, String desc, Double precio, Integer stock, String cat) {
        ProductoDTO p = new ProductoDTO();
        p.setName(nombre); 
        p.setSku(sku);
        p.setDescription(desc); 
        p.setPrice(precio); 
        p.setStock(stock);
        p.setCategory(cat);
        return p;
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

