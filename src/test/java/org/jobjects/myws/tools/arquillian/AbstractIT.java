package org.jobjects.myws.tools.arquillian;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jobjects.myws.tools.wildfly.CliUtils;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public abstract class AbstractIT {
  private static Logger LOGGER = Logger.getLogger(AbstractLocalIT.class.getName());
  public static final String SOURCES_MAIN_JAVA_DIR = "src/main/java";
  public static final String SOURCES_MAIN_RESOURCES_DIR = "src/main/resources";
  public static final String SOURCES_MAIN_WEB_DIR = "src/main/webapp";
  public static final String SOURCES_TEST_JAVA_DIR = "src/test/java";
  public static final String SOURCES_TEST_RESOURCES_DIR = "src/test/resources";

  // public static class MyFilter implements Filter<ArchivePath> {
  //
  // public MyFilter() {
  // }
  //
  // @Override
  // public boolean include(ArchivePath archivePath) {
  // boolean returnValue=!StringUtils.endsWith(archivePath.get(), "Test.class")
  // && !StringUtils.startsWith(archivePath.get(),
  // "org/jobjects/myws/tools/arquillian");
  // //if(returnValue)
  // LOGGER.finest("archivePath:" + archivePath.get() +" "+returnValue);
  // return returnValue;
  // }
  //
  // }

  public static WebArchive createTestableDeployment() {
    WebArchive war = null;
    try {
      war = ShrinkWrap.create(WebArchive.class);
      war.addAsWebResource(new File("src/main/webapp/index.html"), "index.html");
      addStandardFileInWebInfResource(war);
      war.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
      war.addAsManifestResource(new File(SOURCES_MAIN_RESOURCES_DIR + "/META-INF/persistence.xml"), "persistence.xml");

      PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
      File[] libs = pom.importDependencies(ScopeType.COMPILE).resolve().withTransitivity().asFile();
      war.addAsLibraries(libs);
      // File[] commons_lang3 =
      // pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile();
      // war.addAsLibraries(commons_lang3);
      addAllPackages(war, "org.jobjects", new File(SOURCES_MAIN_JAVA_DIR + "/org/jobjects"));
      addAllResources(war, SOURCES_MAIN_RESOURCES_DIR);
      addAllPackages(war, "org.jobjects", new File(SOURCES_TEST_JAVA_DIR + "/org/jobjects"));
      addAllResources(war, SOURCES_TEST_RESOURCES_DIR);
      //org/jobjects/random-users.json
      
      war.as(ZipExporter.class).exportTo(new File("target/myPackage.war"), true);

      LOGGER.finest("==> War name :" + war.toString(Formatters.VERBOSE));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    return war;
  }

  /**
   * Add all packages starting with given prefix from given path.
   *
   * @param war
   *          war archive to add packages to
   * @param prefix
   *          base package
   * @param dir
   *          directory for the base package
   */
  protected static void addAllPackages(WebArchive war, String prefix, File dir) {
    LOGGER.finest("Package add:" + prefix);
    // war.addPackage(prefix);
    // war.addPackages(false,
    // Filters.exclude("(.*Test.class)|(.*"+StringUtils.replace(AbstractIT.class.getPackage().getName(),
    // ".", "\\.")+".*class)"), prefix);
    // war.addPackages(false,
    // Filters.exclude("(.*)Test.class|^"+StringUtils.replace(AbstractIT.class.getPackage().getName(),
    // ".", String.valueOf(File.separatorChar))+"(.*)"), prefix);
    // war.addPackages(false,
    // Filters.exclude("(.*)Test.class|"+AbstractIT.class.getPackage().getName()+"(.*)"),
    // prefix);
    // war.addPackages(false,
    // Filters.exclude("(.*)Test.class|"+AbstractIT.class.getPackage().getName()+"(.*)"),
    // prefix);
    war.addPackages(false, Filters.exclude(CliUtils.class.getPackage()), prefix);
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        addAllPackages(war, prefix + "." + file.getName(), file);
      }
    }
  }

  /**
   * Add all resources from the given directory, recursively. Only adds
   * subdirectories when they start with a lower case letter
   *
   * @param war
   *          war archive to add packages to
   * @param directory
   *          directory with resources to add
   */
  protected static void addAllResources(WebArchive war, String directory) {
    for (File file : new File(directory).listFiles()) {
      // pathname.pathname.isFile() ||
      // Character.isLowerCase(pathname.getName().charAt(0))
      if (file.isFile()) {
      }
      addAllResources(war, "", file);
    }
  }

  protected static void addAllResources(WebArchive war, String prefix, File dir) {
    if (dir.isDirectory()) {
      prefix += dir.getName() + "/";
      for (File file : dir.listFiles()) {
        addAllResources(war, prefix, file);
      }
    } else {
      LOGGER.finest("Ressource add:" + prefix + dir.getName());
      war.addAsResource(dir, prefix + dir.getName());
    }
  }

  protected static void addStandardFileInWebInfResource(WebArchive war) {
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/beans.xml", "beans.xml");
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/web.xml", "web.xml");
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/ejb-jar.xml", "ejb-jar.xml");
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/faces-config.xml", "faces-config.xml");
    addFileInWebInfResource(war, SOURCES_TEST_RESOURCES_DIR + "/jboss-ejb3.xml", "jboss-ejb3.xml");
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/jboss-web.xml", "jboss-web.xml");
    addFileInWebInfResource(war, SOURCES_MAIN_WEB_DIR + "/WEB-INF/jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
  }

  protected static void addFileInWebInfResource(WebArchive war, String resource, String resourceName) {
    // war.addAsWebInfResource(new File(SOURCES_TEST_RESOURCES_DIR +
    // "/jboss-ejb3.xml"), "jboss-ejb3.xml")
    try {
      Path pathResource = Paths.get(new File(resource).toURI());
      if (Files.isReadable(pathResource)) {
        war.addAsWebInfResource(pathResource.toFile(), resourceName);
      } else {
        LOGGER.finest("Resource : " + resource + " is not readable.");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

  }

}
