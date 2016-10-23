package org.jobjects.myws.file;

import java.util.logging.Logger;

import org.jboss.logmanager.Level;
import org.junit.Assert;
import org.junit.Test;

public class SHACheckSumExampleTest {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());
  
  @Test
  public void testMain() {

    //String[] args= {"D:/Utilisateurs/Mickael Patron/Documents/java/github/myws/target/classes/nofile.txt"};
    String[] args= {"target/classes/nofile.txt"};
    try {
      SHACheckSumExample.main(args);
      Assert.assertTrue(true);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.assertTrue(false);
    }
  }

}
