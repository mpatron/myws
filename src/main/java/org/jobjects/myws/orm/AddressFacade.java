package org.jobjects.myws.orm;

import java.util.List;

import org.jobjects.myws.user.Address;
import org.jobjects.myws.user.User;

public interface AddressFacade extends Facade<Address> {
  public List<Address> findByFirstName(final User user);
}
