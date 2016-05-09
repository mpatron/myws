package org.jobjects.myws.email;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang3.StringUtils;

@Singleton
@Lock(LockType.READ)
public class SingletonDomainRepository implements DomainRepository {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  private LinkedList<String> domains;
  private DirContext dirContext;

  @PostConstruct
  void initialize() {
    domains = new LinkedList<String>();

    try {
      Hashtable<String, String> env = new Hashtable<String, String>();
      env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
      dirContext = new InitialDirContext(env);
    } catch (NamingException e) {
      String m = e.getMessage();
      String message = "+" + StringUtils.repeat("=", 78) + "+" + System.lineSeparator();
      message += "!" + StringUtils.repeat(" ", 78) + "!" + System.lineSeparator();
      message += "!" + StringUtils.repeat(" ", (78 - StringUtils.length(m)) / 2) + m + StringUtils.repeat(" ", (78 - StringUtils.length(m)) / 2) + "!"
          + System.lineSeparator();
      message += "!" + StringUtils.repeat(" ", 78) + "!" + System.lineSeparator();
      message += "+" + StringUtils.repeat("=", 78) + "+" + System.lineSeparator();
      LOGGER.log(Level.SEVERE, message, e);
    }

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

  public DirContext getDirContext() {
    return dirContext;
  }
}