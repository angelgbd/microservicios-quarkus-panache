package com.itson.msordenes.entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "detalle_ordenes", joinColumns = @JoinColumn(name = "orden_id"))
    @OrderColumn(name = "posicion")
    public List<DetalleOrden> productos = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal total;

    @Column(name = "fecha_creacion", nullable = false)
    public Instant fechaCreacion;

    public Orden() {
    }
}
