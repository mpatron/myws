package org.jobjects.myws.user;

import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jobjects.myws.tools.log.JObjectsLogFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSonImpTest {
  private static Logger LOGGER = Logger.getLogger(JSonImpTest.class.getName());

  private static List<User> users = new ArrayList<>();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    JObjectsLogFormatter.initializeLogging();
    final String filePathname = "org/jobjects/random-users.json";
    try {
      URL url = ClassLoader.getSystemResource(filePathname);
      if (url != null) {
        Path path = Paths.get(url.toURI());
        if (Files.isReadable(path)) {
          JsonReader parser = Json.createReader(new FileReader(path.toAbsolutePath().toString()));
          JsonObject jsonObject = parser.readObject();
          JsonArray results = jsonObject.getJsonArray("results");
          results.stream().forEach(obj -> {
            JsonObject prof = (JsonObject) obj;
            JsonObject name = prof.getJsonObject("name");
            User user = new User();
            user.setFirstName(name.getString("first"));
            user.setLastName(name.getString("last"));
            user.setEmail(prof.getString("email"));
            users.add(user);
          });
        } else {
          LOGGER.severe("Le fichier " + filePathname + " est illisible : " + path.toAbsolutePath());
        }
      } else {
        LOGGER.severe("Le chemin d'accès à " + filePathname + " est introuvable.");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

  }

  @Test
  public void test() {
    users.stream().forEach(u -> {
      LOGGER.info("first=" + u.getFirstName() + " last=" + u.getLastName() + " email=" + u.getEmail());
    });
  }

}
