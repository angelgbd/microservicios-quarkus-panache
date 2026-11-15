package com.itson.msordenes.entidades;

import java.math.BigDecimal;

/** DTO que representa la respuesta del microservicio de productos. */
public class Producto {
    public Long id;
    public String name;
    public BigDecimal price;

    public Producto() {
    }
}
