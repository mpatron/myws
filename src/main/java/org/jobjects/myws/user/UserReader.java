package org.jobjects.myws.user;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class UserReader implements MessageBodyReader<User> {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Override
  public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    return User.class.isAssignableFrom(type);
  }

  @Override
  public User readFrom(Class<User> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in)
      throws IOException, WebApplicationException {
    User user = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      user = mapper.readValue(in, User.class);
      LOGGER.finest("in -> user as json : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
    } catch (Exception e) {
      throw new WebApplicationException(e);
    }
    return user;
  }
}
