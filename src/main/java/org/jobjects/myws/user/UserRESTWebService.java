package org.jobjects.myws.user;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jobjects.myws.orm.AddressFacade;
import org.jobjects.myws.orm.UserFacade;
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

  @EJB
  UserFacade userFacade;

  @EJB
  AddressFacade addressFacade;

  @ApiOperation(value = "Stocker un user.")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "cas nominal."),
          @ApiResponse(code = 403, message = "Interdiction d'accès."),
          @ApiResponse(code = 500, message = "Erreur interne.") })
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(User user, @Context SecurityContext securityContext) {
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
      if (violations.size() == 0) {
        List<Address> addresses = user.getAddress();
        if (0 == addresses.size()) {
          userFacade.create(user);
        } else {
          user.setAddress(null);
          userFacade.create(user);
          for (Address address : addresses) {
            address.setUser(user);
            addressFacade.create(address);
          }
          user = userFacade.find(user.getId());
          // user.setAddress(addresses);
        }
        returnValue = Response.ok(user, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
      } else {
        LOGGER.log(Level.WARNING, sb.toString());
        returnValue = Response.status(Response.Status.BAD_REQUEST).encoding("UTF-8").entity(sb.toString()).build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(User user, @Context SecurityContext securityContext) {
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
      if (violations.size() == 0) {
        List<Address> addresses = user.getAddress();
        if (0 == addresses.size()) {
          userFacade.save(user);
        } else {
          user.setAddress(null);
          userFacade.save(user);
          for (Address address : addresses) {
            address.setUser(user);
            addressFacade.save(address);
          }
          user = userFacade.find(user.getId());
          // user.setAddress(addresses);
        }
        returnValue = Response.ok(user, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
      } else {
        LOGGER.log(Level.WARNING, sb.toString());
        returnValue = Response.status(Response.Status.BAD_REQUEST).encoding("UTF-8").entity(sb.toString()).build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  @DELETE
  @Path("{user.email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("user.email") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {

      User user = userFacade.findByEmail(email);
      if (user != null) {
        userFacade.remove(user);
        returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
      } else {
        returnValue = Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
    return returnValue;
  }

  @GET
  @Path("{userEmail}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response show(@PathParam("userEmail") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      User user = userFacade.findByEmail(email);
      if (user != null) {
        returnValue = Response.ok(user, MediaType.APPLICATION_JSON).build();
      } else {
        returnValue = Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

}
