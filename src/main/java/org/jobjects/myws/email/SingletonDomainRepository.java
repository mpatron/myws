package org.jobjects.myws.email;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@Singleton
@Lock(LockType.READ)
public class SingletonDomainRepository implements DomainRepository {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  private Map<String, Boolean> domains;
  private DirContext dirContext;

  @PostConstruct
  void initialize() {
    domains = new HashMap<>();

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
  public void add(String domain, boolean isValid) {
    domains.put(domain, isValid);
  }

  @Override
  public int count() {
    return domains.size();
  }

  @Override
  public boolean contains(String domain) {
    return domains.get(domain) != null;
  }

  @Override
  public boolean remove(String domain) {
    return domains.remove(domain);
  }

  @Override
  public boolean getValidityDomain(String domain) {
    return BooleanUtils.isTrue(domains.get(domain));
  }

  public DirContext getDirContext() {
    return dirContext;
  }
  
  public boolean validateEmail(String email) {
    // string character check
    if (!email.matches("[^@]+@([-\\p{Alnum}]+\\.)*\\p{Alnum}+")) {
      //Please check you are using only valid characters in the email address entered.
      LOGGER.log(Level.WARNING, String.format("Email validation : regex unsatisfy %s", email));
      return false;
    }
      
    String[] temp = email.split("@");
    //String user = temp[0];
    String hostname = temp[1];

    // is it one of the common domains.
    String[] hosts = { "gmail.com", "hotmail.com", "live.com", "googlemail.com", "yahoo.com" };
    for (String host : hosts) {
      if (hostname.trim().equalsIgnoreCase(host))
        return true; // if the domain is in the host list then finish
    }
    // now that it is not a common domain
    if (!contains(hostname)) {
      try {
        DirContext ictx = getDirContext();
        Attributes attrs = ictx.getAttributes(hostname, new String[] { "MX", "A" });
        System.out.println("" + attrs);
        Attribute attr = attrs.get("mx");
        if (attr == null) {
          // This domain does not exist."
          add(hostname, false);
          LOGGER.log(Level.WARNING, String.format("Email validation : domainname unsatisfy %s", hostname));
          return false; 
        } else {
          add(hostname, true);
          return true;// we have found records we are happy.
        }
      } catch (NamingException e) {
        LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        return false;
      }
    } else {
      return true;// we have found records in cash.
    }
  }

}