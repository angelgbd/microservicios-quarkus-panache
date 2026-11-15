package com.itson.msordenes.repositorios;

import com.itson.msordenes.entidades.Orden;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrdenRepository implements PanacheRepositoryBase<Orden, Long> {
    @Transactional
    public Orden guardar(Orden orden) {
        persist(orden);
        return orden;
    }
}
