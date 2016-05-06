package org.jobjects.myws;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jobjects.myws.rest.Tracked;

/**
 * @author Mickaël PATRON 2014
 **/
@Path("/lapp")
@RequestScoped
@Tracked
public class MonWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Inject
  UserRepository userRepository;
  
  @Path("/mafonction")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public Response mafonction(@QueryParam("param1") String param1, @QueryParam("param2") String param2) {
    Response returnValue = null;
    try {
      JsonObjectBuilder jsonReturnValue = Json.createObjectBuilder();
      JsonObjectBuilder json = Json.createObjectBuilder();
      json.add("param1", param1);
      json.add("param2", param2);
      jsonReturnValue.add("return", json);
      String jsonStr = jsonReturnValue.build().toString();
      returnValue = Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  @Path("/mafonction2")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response mafonction2() {
    JsonObjectBuilder json = Json.createObjectBuilder();
    json.add("retour", ""+(userRepository!=null));
    String jsonStr = json.build().toString();
    return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
  }

  
}
