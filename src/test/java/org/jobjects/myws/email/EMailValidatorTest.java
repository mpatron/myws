package org.jobjects.myws.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mickaël PATRON 2014
 **/
@RunWith(Arquillian.class)
public class EMailValidatorTest extends AbstractLocalIT {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @EJB
  DomainRepository instance;

  @Test
  public void testValidateEmail() {
    try {
      Assert.assertTrue(instance.validateEmail("toto"));
    } catch (NamingException e) {
      LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
      Assert.assertTrue(true);
    }
    try {
      Assert.assertTrue(instance.validateEmail("toto@gmail.com"));
    } catch (NamingException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.assertTrue(false);
    }
    try {
      Assert.assertTrue(instance.validateEmail("toto@softcomputing.com"));
    } catch (NamingException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.assertTrue(false);
    }
  }

}
