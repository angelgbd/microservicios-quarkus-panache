package com.itson.msordenes.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class DetalleOrden {
    @Column(name = "producto_id", nullable = false)
    public Long id;

    @Column(name = "producto_nombre", nullable = false, length = 120)
    public String name;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    public BigDecimal price;

    public DetalleOrden() {
    }

    public DetalleOrden(Producto producto) {
        this.id = producto.id;
        this.name = producto.name;
        this.price = producto.price;
    }
}
