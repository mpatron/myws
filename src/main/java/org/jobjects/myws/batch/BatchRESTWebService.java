package org.jobjects.myws.batch;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jobjects.myws.rest.Tracked;

import io.swagger.annotations.Api;

/**
 * @author Mickaël PATRON 2014
 **/
@Api(value = "/batch", description = "Operations about batch test")
@Path("/batch")
@RequestScoped
@Tracked
public class BatchRESTWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  public static void main(String[] args) {
    BatchRESTWebService batchMain = new BatchRESTWebService();
    batchMain.dojob(null);
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response dojob(@Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      JobOperator jobOperator = BatchRuntime.getJobOperator();
      Properties props = new Properties();
      jobOperator.start("myJob", props);
      returnValue = Response.ok(MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }
}
