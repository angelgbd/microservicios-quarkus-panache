package com.itson.msordenes.servicios;

import com.itson.msordenes.entidades.Producto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "productos-api")
@Path("/productos")
public interface ProductoServiceClient {
    @GET
    @Path("/{id}")
    Producto obtenerPorId(@PathParam("id") Long id);
}
