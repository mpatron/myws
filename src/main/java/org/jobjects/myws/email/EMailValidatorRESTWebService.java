package org.jobjects.myws.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jobjects.myws.rest.Tracked;

import io.swagger.annotations.Api;

@Api(value = "/email", description = "Operations about email")
@Path("/email")
@RequestScoped
@Tracked
public class EMailValidatorRESTWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @EJB
  DomainRepository domainRepository;

  @GET
  @Path("/{user.email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response isValideEmail(@PathParam("user.email") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      if (domainRepository.validateEmail(email)) {
        returnValue = Response.ok(true, MediaType.APPLICATION_JSON).build();
      } else {
        returnValue = Response.ok(false, MediaType.APPLICATION_JSON).build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }

  
}
