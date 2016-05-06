package org.jobjects.myws.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.jobjects.myws.User;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class UserReader implements MessageBodyReader<User> {

  @Override
  public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    return User.class.isAssignableFrom(type);
  }

  @Override
  public User readFrom(Class<User> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in)
      throws IOException, WebApplicationException {
    User user = new User();

    try {
      InputStreamReader reader = new InputStreamReader(in, "UTF-8");
      JsonReader jsonReader = Json.createReader(reader);
      JsonObject jsonObject = jsonReader.readObject();
      user.setEmail(jsonObject.getString("email"));
      user.setFirstName(jsonObject.getString("firstName"));
      user.setLastName(jsonObject.getString("lastName"));
    } catch (Exception e) {
      throw new WebApplicationException(e);
    }
    return user;
  }
}
