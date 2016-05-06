package org.jobjects.myws;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class EMailValidator {
  public static void main(String[] args) throws NamingException {
    EMailValidator instance = new EMailValidator();
    instance.validateEmail("toto@softcomputing.com");
    
  }
  public boolean validateEmail(String email) throws NamingException{
    // string character check
    if(! email.matches("[^@]+@([-\\p{Alnum}]+\\.)*\\p{Alnum}+"))
      throw new NamingException("please check you are using only valid characters in the email address entered.");
    String[] temp = email.split("@");
    String user = temp[0]; 
    String hostname = temp[1];
 
    // is it one of the common domains. 
    String [] hosts = {"gmail.com","hotmail.com","live.com","googlemail.com","yahoo.com"};
    for (String host : hosts) {
      if(hostname.trim().equalsIgnoreCase(host))
        return true; // if the domain is in the host list then finish
    }
// now that it is not a common domain
    Hashtable<String, String> env = new Hashtable<String,String>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
    DirContext ictx = new InitialDirContext( env );
    Attributes attrs = ictx.getAttributes( hostname, new String[] {"MX","A"});
    System.out.println(""+attrs);
    Attribute attr = attrs.get( "mx" );
    if( attr == null ){
      throw new NamingException("This domain does not exist.");
    }else{
      return true;// we have found records we are happy.
    }
  }

}
