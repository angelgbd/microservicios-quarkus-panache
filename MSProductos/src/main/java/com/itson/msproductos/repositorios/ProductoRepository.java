package com.itson.msproductos.repositorios;

import com.itson.msproductos.entidades.Producto;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductoRepository implements PanacheRepositoryBase<Producto, Long> {
}
