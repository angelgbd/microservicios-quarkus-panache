package com.itson.msordenes.servicios;

import com.itson.msordenes.entidades.DetalleOrden;
import com.itson.msordenes.entidades.Orden;
import com.itson.msordenes.entidades.Producto;
import com.itson.msordenes.repositorios.OrdenRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/ordenes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrdenResource {
    @Inject
    OrdenRepository ordenes;

    @Inject
    @RestClient
    ProductoServiceClient productosClient;

    /** Recibe exactamente tres ids, por ejemplo: [1, 2, 3]. */
    @POST
    public Response crear(List<Long> idsProductos) {
        if (idsProductos == null || idsProductos.size() != 3
                || idsProductos.stream().anyMatch(id -> id == null || id <= 0)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", "Envía exactamente 3 ids de producto positivos; ejemplo: [1, 2, 3]"))
                    .build();
        }

        List<Producto> productosCatalogo = new ArrayList<>();
        try {
            for (Long id : idsProductos) {
                productosCatalogo.add(productosClient.obtenerPorId(id));
            }
        } catch (WebApplicationException ex) {
            if (ex.getResponse() != null && ex.getResponse().getStatus() == 404) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("mensaje", "Uno o más ids no existen en el catálogo de productos"))
                        .build();
            }
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity(Map.of("mensaje", "No fue posible consultar el microservicio de productos"))
                    .build();
        } catch (ProcessingException ex) {
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity(Map.of("mensaje", "El microservicio de productos no está disponible"))
                    .build();
        }

        Orden orden = new Orden();
        BigDecimal total = BigDecimal.ZERO;
        for (Producto producto : productosCatalogo) {
            orden.productos.add(new DetalleOrden(producto));
            total = total.add(producto.price);
        }
        orden.total = total.setScale(2, RoundingMode.HALF_UP);
        orden.fechaCreacion = Instant.now();

        Orden guardada = ordenes.guardar(orden);
        return Response.status(Response.Status.CREATED).entity(guardada).build();
    }

    @GET
    public List<Orden> listar() {
        return ordenes.listAll();
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") Long id) {
        Optional<Orden> orden = ordenes.findByIdOptional(id);
        return orden.map(value -> Response.ok(value).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("mensaje", "No existe la orden con id " + id))
                        .build());
    }
}
