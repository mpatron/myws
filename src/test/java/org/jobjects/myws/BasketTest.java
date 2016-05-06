package org.jobjects.myws;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws.tools.arquillian.AbstractLocalIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mickaël PATRON 2014
 **/
@RunWith(Arquillian.class)
public class BasketTest extends AbstractLocalIT {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @Inject
  Basket basket;

  @EJB
  UserRepository repo;

  @Test
  public void place_order_should_add_order() {
    LOGGER.info(">>>>>>>>>>>>>>>>><<<< place_order_should_add_order...");

    basket.addItem("sunglasses");
    basket.addItem("suit");
    basket.placeOrder();
    Assert.assertEquals(1, repo.getUserCount());
    Assert.assertEquals(0, basket.getItemCount());

    basket.addItem("raygun");
    basket.addItem("spaceship");
    basket.placeOrder();
    Assert.assertEquals(2, repo.getUserCount());
    Assert.assertEquals(0, basket.getItemCount());
  }

  @Test
  public void order_should_be_persistent() {
    LOGGER.info(">>>>>>>>>>>>>>>>><<<< order_should_be_persistent...");
    Assert.assertEquals(2, repo.getUserCount());
  }
  
 }