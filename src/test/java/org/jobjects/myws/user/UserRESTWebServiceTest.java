package org.jobjects.myws.user;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

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
      if (null != userRestClient.show(user.getEmail())) {
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
      if (null != userRestClient.show(user.getEmail())) {
        userRestClient.delete(user.getEmail());
      }
      User userReturn = userRestClient.create(user);
      user.setLastName("PATRON");
      userReturn = userRestClient.update(user);
      
      Assert.assertEquals(user.getEmail(), userReturn.getEmail());
      Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
      Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      Assert.assertNotNull(userReturn.getId());
      Assert.assertTrue(userReturn.getAddress().size() > 0);

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
      if (null != userRestClient.show(user.getEmail())) {
        userRestClient.delete(user.getEmail());
      }
      User userReturn = userRestClient.create(user);
      /* begin Code inatteignable
       * Il y a une erreur. 
       */
      Assert.assertEquals(user.getEmail(), userReturn.getEmail());
      Assert.assertEquals(user.getFirstName(), userReturn.getFirstName());
      Assert.assertEquals(user.getLastName(), userReturn.getLastName());
      Assert.assertNotNull(userReturn.getId());
      LOGGER.info("testCreateUserDirect2 => OK");
      /* end Code inatteignable */
    } catch (Exception e) {
      Assert.assertTrue(true);
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
