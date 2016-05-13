package org.jobjects.myws.user;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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

import org.apache.commons.lang3.StringUtils;
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
  @ApiResponses(value = { @ApiResponse(code = 200, message = "cas nominal."), @ApiResponse(code = 403, message = "Interdiction d'accès."),
      @ApiResponse(code = 500, message = "Erreur interne.") })
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  //@Consumes(MediaType.CHARSET_PARAMETER+"=UTF-8")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(User user, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<User>> violations = validator.validate(user);
      StringBuffer sb = new StringBuffer(); 
      for (ConstraintViolation<User> violation : violations) {
        sb.append(String.format("%s: %s%n", violation.getPropertyPath(), violation.getMessage()));
        sb.append(System.lineSeparator());
      }      
      if(StringUtils.isEmpty(sb.toString())) {
        userRepository.addUser(user);
        returnValue = Response.ok(user, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
      } else {
        LOGGER.log(Level.WARNING, sb.toString());
        returnValue = Response.status(Response.Status.EXPECTATION_FAILED).encoding("UTF-8").entity(sb.toString()).build();
      }

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
