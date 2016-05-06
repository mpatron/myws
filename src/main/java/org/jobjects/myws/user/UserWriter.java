package org.jobjects.myws.user;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.jobjects.myws.User;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class UserWriter implements MessageBodyWriter<User> {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Override
  public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    LOGGER.info("type1=" + type1.getTypeName() + " type=" + type.getCanonicalName());
    return User.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(User t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    // As of JAX-RS 2.0, the method has been deprecated and the
    // value returned by the method is ignored by a JAX-RS runtime.
    // All MessageBodyWriter implementations are advised to return -1 from
    // the method.

    return -1;
  }

  @Override
  public void writeTo(User t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out)
      throws IOException, WebApplicationException {
    // JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    // if (null != t.getEmail())
    // jsonObjectBuilder.add("email", t.getEmail());
    // else
    // jsonObjectBuilder.addNull("email");
    // if (null != t.getFirstName())
    // jsonObjectBuilder.add("firstName", t.getFirstName());
    // else
    // jsonObjectBuilder.addNull("firstName");
    // if (null != t.getLastName())
    // jsonObjectBuilder.add("lastName", t.getLastName());
    // else
    // jsonObjectBuilder.addNull("lastName");
    // JsonObject jsonObject = jsonObjectBuilder.build();
    // JsonWriter jsonWriter = Json.createWriter(out);
    // jsonWriter.writeObject(jsonObject);

    JsonGenerator gen = Json.createGenerator(out);
    gen.writeStartObject();
    if (null == t.getEmail())
      gen.writeNull("email");
    else
      gen.write("email", t.getEmail());
    if (null == t.getFirstName())
      gen.writeNull("firstName");
    else
      gen.write("firstName", t.getFirstName());
    if (null == t.getLastName())
      gen.writeNull("lastName");
    else
      gen.write("lastName", t.getLastName());
    gen.writeEnd();
    gen.flush();
  }
}
