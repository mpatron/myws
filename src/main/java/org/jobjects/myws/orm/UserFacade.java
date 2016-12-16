package org.jobjects.myws.orm;

import java.util.List;
import org.jobjects.myws.user.User;

/**
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
public interface UserFacade extends Facade<User> {
  /**
   * La recherche par email.
   * @param firstName
   *          Paramètre
   * @return La liste des User ayant le prénom passé en paraùètre.
   */
  List<User> findByFirstName(final String firstName);

  /**
   * La recherche par prénom.
   * @param email
   *          Paramètre
   * @return La liste des User ayant l'email passé en paraùètre.
   */
  User findByEmail(final String email);
}
