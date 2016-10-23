package org.jobjects.myws.email;

import javax.ejb.Local;
import javax.naming.directory.DirContext;

@Local
public interface DomainRepository {
  DirContext getDirContext();

  void add(String domain, boolean isValid);

  boolean getValidityDomain(String domain);

  int count();

  boolean contains(String domain);

  boolean remove(String domain);

  public boolean validateEmail(String email);
}