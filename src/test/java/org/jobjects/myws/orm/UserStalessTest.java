package org.jobjects.myws.orm;

import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.jobjects.myws.user.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

//@Ignore
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
    user.setEmail("mpt@gmail.com");
    user.setFirstName("Mickaël");
    user.setLastName("Patron");
    userFacade.create(user);
    User user2 = userFacade.find(user.getId());
    Assert.assertNotNull(user2);

    List<User> users = userFacade.findByFirstName("Mickaël");
    for (User user3 : users) {
      Assert.assertNotNull(user3);
    }
  }

  @Test
  public void testSave() {
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      List<User> users2 = userFacade.findByFirstName("Mickaël");
      for (User user : users2) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    }
  }

  @Test
  public void testRemove() {
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        userFacade.remove(user);
      }
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 0);
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 0);
    }
  }

  @Test
  public void testFind() {
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        User user1 = userFacade.find(user.getId());
        Assert.assertTrue(StringUtils.equals(user1.getId(), user.getId()));
        break;
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertNotNull(userFacade.find(user1.getId()));
      userFacade.remove(user1);
      Assert.assertNull(userFacade.find(user1.getId()));
    }
  }

  @Test
  public void testFindAll() {
    List<User> users = userFacade.findAll();
    if (users.size() > 0) {
      for (User user : users) {
        Assert.assertNotNull(user);
        break;
      }
    } else {
      Assert.assertTrue(userFacade.findAll().size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findAll().size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findAll().size() == 0);
    }
  }

  @Test
  public void testFindRange() {
    List<User> users = userFacade.findRange(0, Integer.MAX_VALUE);
    if (users.size() > 0) {
      for (User user : users) {
        Assert.assertNotNull(user);
        break;
      }
    } else {
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
    }
  }

  @Test
  public void testCount() {
    List<User> users = userFacade.findAll();
    if (users.size() > 0) {
      Assert.assertTrue(true);
    } else {
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findAll().size() > 0);
    }
  }

  @Test
  public void testFindByNamedQuery() {
    List<User> users = userFacade.findByNamedQuery(User.FIND_BY_EMAIL, "mpt@gmail.com");
    if (users.size() > 0) {
      for (User user : users) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findByNamedQuery(User.FIND_BY_EMAIL, "mpt@gmail.com").size() > 0);
    }
  }

}
