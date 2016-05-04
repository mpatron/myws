package org.jobjects.myws.tools.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public abstract class AbstractRemoteIT extends AbstractIT {

  @Deployment(testable = false)
  public static WebArchive deployement() {
    return AbstractIT.createTestableDeployment();
  }

}
