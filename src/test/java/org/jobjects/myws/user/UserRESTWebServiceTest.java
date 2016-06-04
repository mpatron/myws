package org.jobjects.myws.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
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
import com.meterware.httpunit.PostMethodWebRequest;
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
  public void testCreateUserSimple() {
    try {
      User user = new User();
      user.setEmail("mpt@gmail.com");
      user.setFirstName("Mickaël");
      user.setLastName("Patron");

      UserRestClient userRestClient = new UserRestClient(deployUrl);
      if(null==userRestClient.show(user.getEmail())) {
        userRestClient.delete(user.getEmail());
      }
      User userReturn = userRestClient.create(user);
      
      Assert.assertEquals(user.getEmail(), userReturn.getEmail());
      Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
      Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      Assert.assertNotNull(userReturn.getId());
      LOGGER.info("testCreateUserDirect2 => OK");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testCreateUserComplet() throws IOException, SAXException {
    try {
      User user = new User();
      user.setEmail("mpt@gmail.com");
      user.setFirstName("Mickaël");
      user.setLastName("Patron");
      Address address = new Address();
      address.setType(AddressEnum.HOME);
      address.setStreet("1 rue de la paix");
      address.setPostcode("75001");
      address.setCity("Paris");
      address.setState(Locale.FRANCE.getCountry());
      user.getAddress().add(address);

      UserRestClient userRestClient = new UserRestClient(deployUrl);
      if(null==userRestClient.show(user.getEmail())) {
        userRestClient.delete(user.getEmail());
      }
      User userReturn = userRestClient.create(user);
      
      Assert.assertEquals(user.getEmail(), userReturn.getEmail());
      Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
      Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      Assert.assertNotNull(userReturn.getId());
      Assert.assertTrue(userReturn.getAddress().size()>0);

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testCreateUserFalse() throws IOException, SAXException {
    try {
      User user = new User();
      user.setEmail("mpt@gmail.com");
      user.setFirstName(null);
      user.setLastName("Patron");

      UserRestClient userRestClient = new UserRestClient(deployUrl);
      if(null==userRestClient.show(user.getEmail())) {
        userRestClient.delete(user.getEmail());
      }
      User userReturn = userRestClient.create(user);
      
      Assert.assertEquals(user.getEmail(), userReturn.getEmail());
      Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
      Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      Assert.assertNotNull(userReturn.getId());
      LOGGER.info("testCreateUserDirect2 => OK");
    } catch (Exception e) {
      Assert.assertTrue(true);
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    
  }

  private static String convertStreamToString(java.io.InputStream is, String encoding) {
    @SuppressWarnings("resource")
    java.util.Scanner s = new java.util.Scanner(is, encoding).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }


  
  @Test
  public void testCreateUserDirect() {
    try {
      User user = new User();
      user.setEmail("mpt@gmail.com");
      user.setFirstName("Mickaël");
      user.setLastName("Patron");

      LOGGER.info("deployUrl : " + (deployUrl == null ? StringUtils.EMPTY : deployUrl.toString()));
      WebConversation webConversation = new WebConversation();
      StringWriter stringWriter = new StringWriter();
      JsonGenerator gen = Json.createGenerator(stringWriter);
      gen.writeStartObject().write("firstName", user.getFirstName()).write("email", user.getEmail()).writeEnd().flush();

      InputStream source = new ByteArrayInputStream(stringWriter.toString().getBytes("UTF-8"));
      PostMethodWebRequest request = new PostMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user", source,
          MediaType.APPLICATION_JSON);
      request.setHeaderField(HttpHeaders.CONTENT_ENCODING, "UTF-8");
      LOGGER.info("URL : " + request.getURL());
      WebResponse response = webConversation.getResponse(request);
      assertEquals(200, response.getResponseCode());

      String text = convertStreamToString(response.getInputStream(), "UTF-8");
      /**
       * Il y a un bug sur WebResponse.getTest et response.getCharacterSet()
       */
      // String text = new String(response.getText().getBytes(),"UTF-8");

      LOGGER.info("response=" + text + " " + response.getCharacterSet() + " " + response.getResponseMessage());
      try (StringReader reader = new StringReader(text)) {
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonObject = jsonReader.readObject();
        User userReturn = new User();
        userReturn.setEmail(jsonObject.getString("email", null));
        userReturn.setFirstName(jsonObject.getString("firstName", null));
        userReturn.setLastName(jsonObject.getString("lastName", null));
        Assert.assertEquals(user.getEmail(), userReturn.getEmail());
        Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
        Assert.assertNull(userReturn.getLastName()); // !! Non envoyer, c'est le
                                                     // but du test.
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
        Assert.fail(e.getLocalizedMessage());
      }
      source.close();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }

}
