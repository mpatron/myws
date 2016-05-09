package org.jobjects.myws.email;

import javax.ejb.Local;
import javax.naming.directory.DirContext;

@Local
public interface DomainRepository {
  void add(String domain);

  int count();

  boolean contains(String domain);

  boolean remove(String domain);
  
  DirContext getDirContext();
}