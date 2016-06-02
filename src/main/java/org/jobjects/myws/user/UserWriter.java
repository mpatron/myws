package org.jobjects.myws.user;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class UserWriter implements MessageBodyWriter<User> {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    LOGGER.finest("TypeName=" + genericType.getTypeName() + " CanonicalName=" + type.getCanonicalName() + " User.class.isAssignableFrom="
        + User.class.isAssignableFrom(type));
    return User.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(User t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    // http://docs.oracle.com/javaee/7/api/javax/ws/rs/ext/MessageBodyWriter.html#getSize-T-java.lang.Class-java.lang.reflect.Type-java.lang.annotation.Annotation:A-javax.ws.rs.core.MediaType-
    // As of JAX-RS 2.0, the method has been deprecated and the
    // value returned by the method is ignored by a JAX-RS runtime.
    // All MessageBodyWriter implementations are advised to return -1 from
    // the method.

    return -1;
  }

  @Override
  public void writeTo(User user, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm,
      OutputStream out) throws IOException, WebApplicationException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.writeValue(out, user);
    LOGGER.finest("out -> user as json : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));
  }
}
