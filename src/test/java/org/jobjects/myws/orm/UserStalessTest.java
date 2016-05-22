package org.jobjects.myws.orm;

import static org.junit.Assert.fail;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.jobjects.myws.user.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore
@RunWith(Arquillian.class)
public class UserStalessTest extends AbstractLocalIT {

  @EJB
  UserFacade userFacade;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCreate() {
    User user = new User();
    user.setEmail("mpt.softcomputing@gmail.com");
    user.setFirstName("Mickaël");
    user.setLastName("Patron");
    userFacade.create(user);
    User user2 =userFacade.find(user.getId());
    Assert.assertNotNull(user2);
    
    List<User> users =  userFacade.findByNamedQuery(User.FIND_BY_FIRSTNAME, "Mickaël");
    for (User user3 : users) {
      Assert.assertNotNull(user3);
    }
  }

  @Test
  public void testSave() {
    User user = new User();
    user.setEmail("mpt.softcomputing@gmail.com");
    user.setFirstName("Mickaël");
    user.setLastName("Patron");
    userFacade.create(user);
  }

  @Test
  public void testRemove() {
    fail("Not yet implemented");
  }

  @Test
  public void testFind() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindAll() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindRange() {
    fail("Not yet implemented");
  }

  @Test
  public void testCount() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindByNamedQuery() {
    fail("Not yet implemented");
  }

}
