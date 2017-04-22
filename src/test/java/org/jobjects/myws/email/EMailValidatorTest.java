package org.jobjects.myws.email;

import java.util.logging.Logger;

import javax.ejb.EJB;

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
    LOGGER.info("Start tests email");
    Assert.assertFalse(instance.validateEmail("toto"));
    Assert.assertTrue(instance.validateEmail("toto@gmail.com"));
    Assert.assertTrue(instance.validateEmail("toto@softcomputing.com"));
    LOGGER.info("Fin tests email");
  }

}
