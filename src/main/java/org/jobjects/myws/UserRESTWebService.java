package org.jobjects.myws;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jobjects.myws.rest.Tracked;

/**
 * @author Mickaël PATRON 2014
 **/
@Path("/user")
@RequestScoped
@Tracked
public class UserRESTWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Inject
  UserRepository userRepository;
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(User user, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      userRepository.addUser(user);
      returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateUser(User user, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      userRepository.addUser(user);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  @DELETE
  @Path("/{user.email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUser(@PathParam("user.email") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      userRepository.deleteUser(email);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }
  
  @GET
  @Path("/{user.email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response showUser(@PathParam("user.email") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      userRepository.getUser(email);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }
  
  
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
