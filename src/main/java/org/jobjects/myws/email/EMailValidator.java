package org.jobjects.myws.email;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
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
public class EMailValidator {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @EJB
  DomainRepository domainRepository;

  @GET
  @Path("/{user.email}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response isValideEmail(@PathParam("user.email") String email, @Context SecurityContext securityContext) {
    Response returnValue = null;
    try {
      if (validateEmail(email)) {
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

  public boolean validateEmail(String email) throws NamingException {
    // string character check
    if (!email.matches("[^@]+@([-\\p{Alnum}]+\\.)*\\p{Alnum}+"))
      throw new NamingException("please check you are using only valid characters in the email address entered.");
    String[] temp = email.split("@");
    //String user = temp[0];
    String hostname = temp[1];

    // is it one of the common domains.
    String[] hosts = { "gmail.com", "hotmail.com", "live.com", "googlemail.com", "yahoo.com" };
    for (String host : hosts) {
      if (hostname.trim().equalsIgnoreCase(host))
        return true; // if the domain is in the host list then finish
    }
    // now that it is not a common domain
    if (!domainRepository.contains(hostname)) {
//      Hashtable<String, String> env = new Hashtable<String,String>();
//      env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
//      DirContext ictx = new InitialDirContext( env );
      //DirContext ictx = new InitialDirContext();
      DirContext ictx = domainRepository.getDirContext();
      Attributes attrs = ictx.getAttributes(hostname, new String[] { "MX", "A" });
      System.out.println("" + attrs);
      Attribute attr = attrs.get("mx");
      if (attr == null) {
        throw new NamingException("This domain does not exist.");
      } else {
        domainRepository.add(hostname);
        return true;// we have found records we are happy.
      }
    } else {
      return true;// we have found records in cash.
    }
  }
  
}
