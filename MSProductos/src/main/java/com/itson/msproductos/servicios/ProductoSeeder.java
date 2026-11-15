package com.itson.msproductos.servicios;

import com.itson.msproductos.entidades.Producto;
import com.itson.msproductos.repositorios.ProductoRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class ProductoSeeder {
    @Inject
    ProductoRepository productos;

    @Transactional
    void alIniciar(@Observes StartupEvent evento) {
        if (productos.count() > 0) {
            return;
        }
        productos.persist(new Producto("Laptop Gaming", new BigDecimal("1500.00")));
        productos.persist(new Producto("Teclado mecánico", new BigDecimal("85.50")));
        productos.persist(new Producto("Mouse inalámbrico", new BigDecimal("35.25")));
    }
}
