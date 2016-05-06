package org.jobjects.myws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * @author Mickaël PATRON 2014
 **/
@RunWith(Arquillian.class)
public class UserRESTWebServiceTest extends AbstractRemoteIT {

  private static Logger LOGGER = Logger.getLogger(UserRESTWebServiceTest.class.getName());

  @ArquillianResource
  private URL deployUrl;

  @Test
  public void testCreateUser() throws IOException, SAXException {
    try {
      Client client = ClientBuilder.newClient();
      LOGGER.info("deployUrl : " + (deployUrl == null ? StringUtils.EMPTY : deployUrl.toString()));
      WebTarget webTarget = client.target(deployUrl.toString().replace("8080", "9143") + "api/user");
      LOGGER.info("URI : " + webTarget.getUri());
      User user = new User();
      user.setEmail("mpt.softcomputing@gmail.com");
      user.setFirstName("Mickaël");
      user.setLastName("Patron");
      Response response = webTarget.request().post(Entity.json(user));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        User userReturn = response.readEntity(User.class);
        LOGGER.info("userReturn=" + userReturn);
        Assert.assertEquals(user.getEmail(), userReturn.getEmail());
        Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
        Assert.assertEquals(user.getLastName(), userReturn.getLastName());
        // String p = response.readEntity(String.class);
        // try (StringReader reader = new StringReader(p)) {
        // JsonReader jsonReader = Json.createReader(reader);
        // JsonObject jsonObject = jsonReader.readObject();
        // returnValue=jsonObject.getBoolean("return");
        // } catch (Exception e) {
        // throw e;
        // }
      } else {
        LOGGER.log(Level.SEVERE, statusType.getReasonPhrase());
        Assert.assertTrue(false);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testMafonction() {
    try {
      LOGGER.info("deployUrl : " + (deployUrl == null ? StringUtils.EMPTY : deployUrl.toString()));
      WebConversation webConversation = new WebConversation();
      InputStream source = new ByteArrayInputStream("une fois rien...".getBytes());
      PutMethodWebRequest request = new PutMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user/mafonction?param1=pArAm_1&param2=PaRam_2",
          source, MediaType.APPLICATION_JSON);
      LOGGER.info("URL : " + request.getURL());
      WebResponse response = webConversation.getResponse(request);
      assertEquals(200, response.getResponseCode());
      LOGGER.info(response.getText());
      assertTrue(response.getText().contains("pArAm_1"));
      assertTrue(response.getText().contains("PaRam_2"));
      source.close();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testMafonction2() throws IOException, SAXException {
    WebConversation webConversation = new WebConversation();
    InputStream source = new ByteArrayInputStream("de fois rien...".getBytes());
    GetMethodWebRequest request = new GetMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user/mafonction2");
    WebResponse response = webConversation.getResponse(request);
    assertEquals(200, response.getResponseCode());
    LOGGER.info("Json :" + response.getText());
    assertTrue(response.getText().contains("true"));
    source.close();
  }
}
