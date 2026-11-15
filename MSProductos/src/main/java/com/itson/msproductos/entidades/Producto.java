package com.itson.msproductos.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 120)
    public String name;

    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal price;

    public Producto() {
    }

    public Producto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
