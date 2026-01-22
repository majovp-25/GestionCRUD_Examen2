package com.examen2.GestionCRUD.service;

import com.examen2.GestionCRUD.dto.ProductoDTO;
import com.examen2.GestionCRUD.model.Producto;
import com.examen2.GestionCRUD.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Producto producto = convertirAEntidad(dto);

        // LOGICA DE SKU AUTOMÁTICO (Solo si es nuevo)
        if (producto.getId() == null) {
            String skuGenerado = generarSkuAutomatico(dto.getCategory(), dto.getName());
            producto.setSku(skuGenerado);
        }

        producto = productoRepository.save(producto);
        return convertirADTO(producto);
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

    @Override
    public void eliminarTodos() {
        productoRepository.vaciarTablaYReiniciarIds();
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

    // --- MÉTODO PRIVADO NUEVO PARA GENERAR SKU AUTOMÁTICO ---
private String generarSkuAutomatico(String categoria, String nombre) {
        if (categoria == null) categoria = "GEN";
        if (nombre == null) nombre = "PROD";

        String catCode = categoria.length() >= 3 ? categoria.substring(0, 3).toUpperCase() : categoria.toUpperCase();
        String nomCode = nombre.length() >= 3 ? nombre.substring(0, 3).toUpperCase() : nombre.toUpperCase();

        // Buscamos el último producto REAL en la base de datos
        Optional<Producto> ultimoProducto = productoRepository.findTopByCategoriaOrderByIdDesc(categoria);

        long siguienteNumero = 1; // Si no hay ninguno, empezamos en 1

        if (ultimoProducto.isPresent()) {
            String ultimoSku = ultimoProducto.get().getSku();
            // El SKU es tipo: TEC-LAP-008. Separamos por el guion "-"
            String[] partes = ultimoSku.split("-");
            if (partes.length == 3) {
                try {
                    // Tomamos la última parte ("008") y la convertimos a número
                    long ultimoNumero = Long.parseLong(partes[2]);
                    siguienteNumero = ultimoNumero + 1;
                } catch (NumberFormatException e) {
                    // Si el SKU manual tenía formato raro, usamos count como plan B
                    siguienteNumero = productoRepository.countByCategoria(categoria) + 1; 
                }
            }
        }

        return catCode + "-" + nomCode + "-" + String.format("%03d", siguienteNumero);
    }
}