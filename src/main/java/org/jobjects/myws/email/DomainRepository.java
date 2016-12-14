package org.jobjects.myws.email;

import javax.ejb.Local;
import javax.naming.directory.DirContext;

/**
 * @author Mickaël Patron
 * @version 2016-05-08
 */
@Local
public interface DomainRepository {
  /**
   * Retourne la référence du context.
   * @return le context.
   */
  DirContext getDirContext();

  /**
   * Ajoute un domain en spécifiant s'il est valide ou pas.
   * @param domain
   *          est le fqdn sans le host.
   * @param isValid
   *          indique la validité.
   */
  void add(String domain, boolean isValid);

  /**
   * Retourne un domain valide.
   * @param domain
   *          est le fqdn sans le host.
   * @return un boolean afin d'indiquer sa validité.
   */
  boolean getValidityDomain(String domain);

  /**
   * Retourne le nombre de domain.
   * @return retourne le nombre de domain.
   */
  int count();

  /**
   * Retourne si le domain founi est existant.
   * @param domain
   *          est le fqdn sans le host.
   * @return un boolean afin d'indiquer sa présence.
   */
  boolean contains(String domain);

  /**
   * Supprime le domain founi.
   * @param domain
   *          est le fqdn sans le host.
   * @return un boolean afin d'indiquer si la suppression a fonctionné ou pas.
   */
  boolean remove(String domain);

  /**
   * Vérifie si l'email est valide.
   * @param email
   *          normal
   * @return un boolean afin d'indiquer sa validité.
   */
  boolean validateEmail(String email);
}
