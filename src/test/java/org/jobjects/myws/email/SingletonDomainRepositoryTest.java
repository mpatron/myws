package org.jobjects.myws.email;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SingletonDomainRepositoryTest extends AbstractLocalIT {

  @EJB
  DomainRepository domainRepository;
  
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
  public void testAdd() {
    domainRepository.add("domain", true);
    Assert.assertTrue(domainRepository.count()>0);
  }
  
  @Test
  public void testCount() {
    domainRepository.add("domain", true);
    Assert.assertTrue(domainRepository.count()>0);
  }

  @Test
  public void testContains() {
    domainRepository.add("domain", true);
    Assert.assertTrue(domainRepository.contains("domain"));
  }

  @Test
  public void testRemove() {
    domainRepository.remove("domain");
    Assert.assertFalse(domainRepository.contains("domain"));
  }

}
