package org.jobjects.myws.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jobjects.myws.MonWebService;

/**
 * Dans JEE7, il ne suffit pas de d'utiliser les annotations REST pour le faire
 * fonctionner. Glassfish est fourni avec Jersey, par la dependency est donc en
 * provided. Avec la technique "extends Application " pas la peine de faire
 * la déclartion dans le web.xml.
 * https://jersey.java.net/nonav/documentation/2.0/index.html
 * https://jersey.java.net/nonav/documentation/2.0/deployment.html
 * 
 * @author Mickaël PATRON 2014
 *
 */
@ApplicationPath("api")
public class RestApplicationConfiguration extends Application {
  @Override
  public Set<Class<?>> getClasses() {
      Set<Class<?>> s = new HashSet<Class<?>>();
      s.add(MonWebService.class);
      s.add(TrafficLogger.class);
      return s;
  }
}