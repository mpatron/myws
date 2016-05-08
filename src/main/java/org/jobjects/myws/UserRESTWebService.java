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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Mickaël PATRON 2014
 **/
@Api(value = "/user", description = "Operations about user")
@Path("/user")
@RequestScoped
@Tracked
public class UserRESTWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Inject
  UserRepository userRepository;

  @ApiOperation(value = "Stocker un user.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "cas nominal."),
      @ApiResponse(code = 403, message = "Interdiction d'accès."),
      @ApiResponse(code = 500, message = "Erreur interne.") })
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
      returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
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
      User user = userRepository.deleteUser(email);
      returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
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
      User user = userRepository.getUser(email);
      returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
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
    json.add("retour", "" + (userRepository != null));
    String jsonStr = json.build().toString();
    return Response.ok(jsonStr, MediaType.APPLICATION_JSON).build();
  }

}
