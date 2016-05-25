package org.jobjects.myws.user;

import static org.junit.Assert.fail;

import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSonImpTest {
  private static Logger LOGGER = Logger.getLogger(JSonImpTest.class.getName());

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    final String filePathname = "org/jobjects/random-users.json";
    try {
      URL url = ClassLoader.getSystemResource(filePathname);
      if (url != null) {
        Path path = Paths.get(url.toURI());
        if (Files.isReadable(path)) {
          JsonReader parser = Json.createReader(new FileReader(path.toAbsolutePath().toString()));
          JsonObject jsonObject= parser.readObject();
          JsonArray results=jsonObject.getJsonArray("results");
          results.get

//          InputStream is = new FileInputStream(path.toAbsolutePath().toString());
//          // ...
//          is.close();
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

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    fail("Not yet implemented");
  }

}
