package org.jobjects.myws.file;

import java.util.logging.Logger;

import org.jboss.logmanager.Level;
import org.junit.Test;

public class SHACheckSumExampleTest {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());
  
  @Test
  public void testMain() {

    String[] args= {"D:/Utilisateurs/Mickael Patron/Documents/java/github/myws/target/myws/swagger-ui.js"};
    try {
      SHACheckSumExample.main(args);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

}
