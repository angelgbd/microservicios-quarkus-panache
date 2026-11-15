package com.itson.msproductos.servicios;

import com.itson.msproductos.entidades.Producto;
import com.itson.msproductos.repositorios.ProductoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Path("/productos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {
    @Inject
    ProductoRepository productos;

    @GET
    public List<Producto> listar() {
        return productos.listAll();
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") Long id) {
        return productos.findByIdOptional(id)
                .map(producto -> Response.ok(producto).build())
                .orElseGet(() -> noEncontrado(id));
    }

    @POST
    @Transactional
    public Response crear(Producto producto) {
        if (!esValido(producto)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", "name es obligatorio y price debe ser mayor que cero"))
                    .build();
        }
        producto.id = null;
        productos.persist(producto);
        return Response.status(Response.Status.CREATED).entity(producto).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response actualizar(@PathParam("id") Long id, Producto cambios) {
        if (!esValido(cambios)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", "name es obligatorio y price debe ser mayor que cero"))
                    .build();
        }
        return productos.findByIdOptional(id)
                .map(producto -> {
                    producto.name = cambios.name.trim();
                    producto.price = cambios.price;
                    return Response.ok(producto).build();
                })
                .orElseGet(() -> noEncontrado(id));
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response eliminar(@PathParam("id") Long id) {
        if (!productos.deleteById(id)) {
            return noEncontrado(id);
        }
        return Response.noContent().build();
    }

    private boolean esValido(Producto producto) {
        return producto != null
                && producto.name != null
                && !producto.name.isBlank()
                && producto.price != null
                && producto.price.compareTo(BigDecimal.ZERO) > 0;
    }

    private Response noEncontrado(Long id) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("mensaje", "No existe el producto con id " + id))
                .build();
    }
}
