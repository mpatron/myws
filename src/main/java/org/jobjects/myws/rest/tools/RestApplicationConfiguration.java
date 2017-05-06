package org.jobjects.myws.rest.tools;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jobjects.myws.batch.BatchRESTWebService;
import org.jobjects.myws.email.EMailValidatorRESTWebService;
import org.jobjects.myws.rest.user.UserRESTWebService;
import org.jobjects.myws.rest.user.UserReader;
import org.jobjects.myws.rest.user.UserWriter;

import io.swagger.jaxrs.config.BeanConfig;

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
@ApplicationPath("/api")
public class RestApplicationConfiguration extends Application {
  
  public RestApplicationConfiguration() {
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setTitle("My web Service");
    beanConfig.setVersion("1.0.2");
    beanConfig.setSchemes(new String[] { "http", "https" });
    beanConfig.setHost("localhost:8080");
    beanConfig.setBasePath("/myws/api");
    beanConfig.setResourcePackage("org.jobjects.myws");
    beanConfig.setScan(true);
  }

  @Override
  public Set<Class<?>> getClasses() {
      Set<Class<?>> resources = new HashSet<Class<?>>();
      /* filtering pour swagger */
      resources.add(CORSFilter.class);
      resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
      resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
      /* filtering pour le logging */
      resources.add(CustomRequestWrapperFilter.class);
      resources.add(TrafficLogger.class);
      /* web services */
      resources.add(EMailValidatorRESTWebService.class);
      resources.add(UserRESTWebService.class);
      resources.add(UserWriter.class);
      resources.add(UserReader.class);
      resources.add(BatchRESTWebService.class);
      return resources;
  }
}