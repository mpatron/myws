package org.jobjects.myws.orm;

import java.util.List;

import org.jobjects.myws.user.User;

public interface UserFacade extends Facade<User> {
  List<User> findByFirstName(final String firstName);
  User findByEmail(final String email);
}
