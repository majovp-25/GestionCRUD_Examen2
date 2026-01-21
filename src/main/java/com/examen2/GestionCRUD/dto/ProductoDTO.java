package com.examen2.GestionCRUD.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductoDTO {
    private Long id;
    private String name;        // Se mapeará a "nombre"
    private String sku;         // Se mapeará a "sku"
    private String description; // Se mapeará a "descripcion"
    private Double price;       // Se mapeará a "precio"
    private Integer stock;      // Se mapeará a "stock"
    private String category;    // Se mapeará a "categoria"
}
