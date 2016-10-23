package org.jobjects.myws.email;

import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mickaël PATRON 2014
 **/
@RunWith(Arquillian.class)
public class RestEmailValidatorTest extends AbstractRemoteIT {

  private Logger LOGGER = Logger.getLogger(getClass().getName());
  //private final static String REDIRECT_PORT = "9143";
  private final static String REDIRECT_PORT = "8080";

  @ArquillianResource
  private URL deployUrl;

  @Test
  public void testIsValideEmail() {
    try {
      Client client = ClientBuilder.newClient();
      LOGGER.info("deployUrl : " + (deployUrl == null ? StringUtils.EMPTY : deployUrl.toString()));
      WebTarget webTarget = client
          .target(deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/email/" + URLEncoder.encode("mpt@softcomputing.com", "UTF-8"));
      LOGGER.info("URI : " + webTarget.getUri());
      Response response = webTarget.request().get();
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        String stringReturn = response.readEntity(String.class);
        LOGGER.info("stringReturn=" + stringReturn);
        // Assert.assertEquals(user.getEmail(), userReturn.getEmail());
        // Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
        // Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      } else {
        LOGGER.log(Level.SEVERE, statusType.getReasonPhrase());
        Assert.assertTrue(false);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }
  
   
}
