package com.examen2.GestionCRUD.service;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.model.Producto;
import com.examen2.GestionCRUD.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // --- MÉTODOS DE LA INTERFAZ ---

    @Override
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO) // Convertimos cada producto a DTO
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertirADTO(producto);
    }

    @Override
    public ProductoDTO guardar(ProductoDTO dto) {
        Producto producto = convertirAEntidad(dto); // De DTO a Entidad
        producto = productoRepository.save(producto); // Guardamos en BD
        return convertirADTO(producto); // Devolvemos DTO
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizamos solo los datos que vienen del frontend
        productoExistente.setNombre(dto.getName());
        productoExistente.setDescripcion(dto.getDescription());
        productoExistente.setPrecio(dto.getPrice()); 
        productoExistente.setStock(dto.getStock());
        productoExistente.setCategoria(dto.getCategory());
        productoExistente.setSku(dto.getSku());
        
        // Ajuste manual por si Lombok generó nombres en español:
        productoExistente.setPrecio(dto.getPrice()); 
        
        Producto actualizado = productoRepository.save(productoExistente);
        return convertirADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ID no existe");
        }
        productoRepository.deleteById(id);
    }

    // --- MÉTODOS PRIVADOS PARA MAPEO (TRADUCCIÓN) ---
    // Aquí traducimos el "Inglés de Codigo HTML" al "Español de la BD"

    private ProductoDTO convertirADTO(Producto p) {
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),    
                p.getSku(),
                p.getDescripcion(), 
                p.getPrecio(),      
                p.getStock(),
                p.getCategoria()  
        );
    }

    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto p = new Producto();
        p.setId(dto.getId());
        p.setNombre(dto.getName());
        p.setSku(dto.getSku());
        p.setDescripcion(dto.getDescription());
        p.setPrecio(dto.getPrice());
        p.setStock(dto.getStock());
        p.setCategoria(dto.getCategory());
        return p;
    }
}