package br.com.lucaspaz.testrest.ws;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.StringReader;

/**
 *
 * @author
 */
@Path("")
public class AppWs {

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        return Response
                .ok("Hello World")
                .build();
    }

    @POST
    @Path("to-upper")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(String json) throws BusinessException {

        JsonReader reader = Json.createReader(new StringReader(json));

        String nome = reader.readObject().getString("name");

        if (nome == null || nome.isEmpty()) {
            throw new BusinessException("Name not found");
        }

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("name", nome.toUpperCase()).build();

        return Response
                .ok(jsonResponse.toString())
                .build();
    }

}
