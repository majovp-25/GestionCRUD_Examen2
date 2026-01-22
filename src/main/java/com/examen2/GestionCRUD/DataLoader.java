package com.examen2.GestionCRUD;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IProductoService productoService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando limpieza automatica de la base de datos...");
        
        // 1. LIMPIEZA TOTAL (TRUNCATE)
        // Esto evita el error de "duplicados" y reinicia los IDs a 1
        productoService.eliminarTodos(); 
        
        System.out.println("Base de datos vacia. Cargando datos frescos... 🚀");

        // 2. CARGA DE DATOS
        cargarDatosIniciales();
        
        System.out.println("Sistema listo. 60 Productos cargados desde cero.");
    }

    private void cargarDatosIniciales() {
        // 1. PRODUCTOS DESTACADOS 
        List<ProductoDTO> destacados = Arrays.asList(
            new ProductoDTO(null, "Laptop Gamer Dell", null, "Ryzen 7, 16GB RAM, RTX 3060", 1200.00, 5, "Tecnología"),
            new ProductoDTO(null, "iPhone 15 Pro", null, "Titanio, 256GB", 1100.00, 8, "Tecnología"),
            new ProductoDTO(null, "Monitor LG Ultrawide", null, "34 pulgadas, 144Hz", 450.00, 3, "Tecnología"),
            new ProductoDTO(null, "Teclado Keychron K2", null, "Mecánico, Inalámbrico", 95.00, 15, "Tecnología"),
            new ProductoDTO(null, "Mouse MX Master 3", null, "Ergonómico, Productividad", 80.00, 20, "Tecnología"),
            
            new ProductoDTO(null, "Cafetera Italiana", null, "Acero inoxidable, 6 tazas", 25.00, 12, "Hogar"),
            new ProductoDTO(null, "Juego de Sábanas", null, "Algodón 500 hilos, Queen", 60.00, 10, "Hogar"),
            new ProductoDTO(null, "Aspiradora Robot", null, "Mapeo inteligente, Wifi", 250.00, 4, "Hogar"),
            
            new ProductoDTO(null, "Chaqueta de Cuero", null, "Estilo Biker, Negra", 85.00, 6, "Ropa"),
            new ProductoDTO(null, "Zapatillas Running", null, "Amortiguación Gel, Talla 42", 70.00, 14, "Ropa"),
            new ProductoDTO(null, "Camisa de Lino", null, "Color beige, corte Slim", 35.00, 18, "Ropa"),
            
            new ProductoDTO(null, "Mochila Antirrobo", null, "Impermeable con USB", 55.00, 22, "Accesorios"),
            new ProductoDTO(null, "Reloj Casio Vintage", null, "Digital, Plateado", 45.00, 25, "Accesorios"),
            new ProductoDTO(null, "Balón de Fútbol", null, "Nº 5, Profesional", 30.00, 40, "Deportes"),
            new ProductoDTO(null, "Mat de Yoga", null, "Antideslizante, 6mm", 20.00, 15, "Deportes")
        );

        // Guardar destacados
        destacados.forEach(p -> productoService.guardar(p));

        // 2. RELLENO MASIVO (Hasta llegar a 60 aprox)
        String[] categorias = {"Tecnología", "Hogar", "Ropa", "Accesorios", "Deportes", "Juguetes"};
        
        for (int i = 1; i <= 45; i++) {
            String cat = categorias[i % categorias.length];
            ProductoDTO p = new ProductoDTO();
            p.setName("Producto " + cat + " " + i);
            p.setDescription("Descripción de relleno #" + i);
            p.setPrice(10.00 + i);
            p.setStock((int)(Math.random() * 50) + 1);
            p.setCategory(cat);
            // Sin SKU para que se genere solo
            productoService.guardar(p);
        }
    }
}