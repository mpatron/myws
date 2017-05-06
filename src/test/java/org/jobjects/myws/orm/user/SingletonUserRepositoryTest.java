package org.jobjects.myws.orm.user;

import java.util.logging.Logger;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.orm.user.User;
import org.jobjects.myws.rest.user.UserRepository;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SingletonUserRepositoryTest extends AbstractLocalIT {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @EJB
  UserRepository userRepository;

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
  public void testAddUser() {
    User user = new User();
    user.setEmail("toto.titi@fr.fr");
    user.setFirstName("toto");
    user.setLastName("titi");
    userRepository.addUser(user);
    Assert.assertTrue(userRepository.getUserCount() > 0);
  }

  @Test
  public void testGetUsers() {
    User user = new User();
    user.setEmail("toto.titi@fr.fr");
    user.setFirstName("toto");
    user.setLastName("titi");
    userRepository.addUser(user);
    LOGGER.info(ReflectionToStringBuilder.toString((Object) user, ToStringStyle.JSON_STYLE, false));
    userRepository.getUsers().stream().forEach(u -> {
      LOGGER.info(ReflectionToStringBuilder.toString(u, ToStringStyle.JSON_STYLE, false));
      Assert.assertNotNull(u);
    }

    );
    Assert.assertTrue(userRepository.getUserCount() > 0);
  }

  @Test
  public void testGetUserCount() {
    User user = new User();
    user.setEmail("toto.titi@fr.fr");
    user.setFirstName("toto");
    user.setLastName("titi");
    userRepository.addUser(user);
    Assert.assertTrue(userRepository.getUserCount() > 0);
  }

  @Test
  public void testGetUser() {
    User user1 = new User();
    user1.setEmail("toto.titi@fr.fr");
    user1.setFirstName("toto");
    user1.setLastName("titi");
    userRepository.addUser(user1);
    User user2 = userRepository.getUser("toto.titi@fr.fr");
    Assert.assertTrue(StringUtils.equals(ReflectionToStringBuilder.toString(user1, ToStringStyle.JSON_STYLE, false),
        ReflectionToStringBuilder.toString(user2, ToStringStyle.JSON_STYLE, false)));
  }

  @Test
  public void testDeleteUser() {
    User user1 = new User();
    user1.setEmail("toto.titi@fr.fr");
    user1.setFirstName("toto");
    user1.setLastName("titi");
    userRepository.addUser(user1);
    userRepository.deleteUser("toto.titi@fr.fr");
    Assert.assertTrue(userRepository.getUserCount() == 0);
  }

}
