package org.jobjects.myws.email;

import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
@Lock(LockType.READ)
public class SingletonDomainRepository implements DomainRepository {
  private LinkedList<String> domains;

  @PostConstruct
  void initialize() {
    domains = new LinkedList<String>();
  }

  @Override
  @Lock(LockType.WRITE)
  public void add(String domain) {
    domains.add(domain);
  }

  @Override
  public int count() {
    return domains.size();
  }

  @Override
  public boolean contains(String domain) {
    return domains.contains(domain);
  }

  @Override
  public boolean remove(String domain) {
    return domains.remove(domain);
  }
}