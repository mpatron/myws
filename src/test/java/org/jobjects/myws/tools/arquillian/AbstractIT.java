package org.jobjects.myws.tools.arquillian;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jobjects.myws.tools.log.JObjectsLogFormatter;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public class AbstractIT {
  private static Logger LOGGER = Logger.getLogger(AbstractLocalIT.class.getName());
  public static final String SOURCES_MAIN_JAVA_DIR = "src/main/java";
  public static final String SOURCES_MAIN_RESOURCES_DIR = "src/main/resources";
  public static final String SOURCES_MAIN_WEB_DIR = "src/main/webapp";
  public static final String SOURCES_TEST_JAVA_DIR = "src/test/java";
  public static final String SOURCES_TEST_RESOURCES_DIR = "src/test/resources";

  private static class MyFilter implements Filter<ArchivePath> {

    @Override
    public boolean include(ArchivePath archivePath) {
      LOGGER.fine("archivePath:" + archivePath.get());
      return !StringUtils.endsWith(archivePath.get(), "Test.class") && !StringUtils.startsWith(archivePath.get(), "org/jobjects/myws/tools/arquillian");
    }

  }

  public static WebArchive createTestableDeployment() {
    JObjectsLogFormatter.initializeLogging();
    
    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    File[] commons_lang3 = pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile();
    File[] commons_io = pom.resolve("commons-io:commons-io").withTransitivity().asFile();    
    File[] shrinkwrap_api = pom.resolve("org.jboss.shrinkwrap:shrinkwrap-api").withTransitivity().asFile();
    //File[] active_directory = pom.resolve("com.softcomputing.ad:active-directory").withTransitivity().asFile();

    WebArchive war = ShrinkWrap
        .create(WebArchive.class)
        .addAsWebResource(new File("src/main/webapp/index.html"), "index.html")
        // .addAsWebInfResource(new File(SOURCES_TEST_RESOURCES_DIR + "/jboss-ejb3.xml"), "jboss-ejb3.xml")
        //.addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/jboss-web.xml"), "jboss-web.xml")
        .addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/beans.xml"), "beans.xml")
        .addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/web.xml"), "web.xml")
        //.addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/faces-config.xml"), "faces-config.xml")
        .addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/ejb-jar.xml"), "ejb-jar.xml")
        // .addAsWebInfResource(new File(SOURCES_MAIN_WEB_DIR + "/WEB-INF/jboss-deployment-structure.xml"),
        // "jboss-deployment-structure.xml")
        //.addAsManifestResource(new File(SOURCES_MAIN_WEB_DIR + "/META-INF/MANIFEST.MF"), "MANIFEST.MF")
        //.addAsResource(new File(SOURCES_TEST_RESOURCES_DIR + "/com/softcomputing/rcu/config.properties"), "config.propertiesv")
        .addAsLibraries(commons_lang3)
        .addAsLibraries(commons_io)
        .addAsLibraries(shrinkwrap_api)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        /*.addAsLibraries(active_directory)*/;

//    war.addPackages(false, new MyFilter(), "org.jobjects.myws");
//    war.addPackages(false, new MyFilter(), "org.jobjects.myws.rest");
//    war.addPackages(false, new MyFilter(), "org.jobjects.myws.tools.log");
//    war.addPackages(false, new MyFilter(), "org.jobjects.myws.tools.wildfly");
    addAllPackages(war, "org.jobjects", new File(SOURCES_MAIN_JAVA_DIR + "/org/jobjects"));
    addAllResources(war, SOURCES_MAIN_RESOURCES_DIR);
    addAllPackages(war, "org.jobjects", new File(SOURCES_TEST_JAVA_DIR + "/org/jobjects"));
    // addAllResources(jar, RESOURCES_DIR);

    war.as(ZipExporter.class).exportTo(new File("target/myPackage.war"), true);

    LOGGER.fine("==> War name :" + war.toString(Formatters.VERBOSE));

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
    LOGGER.fine("Package add:" + prefix);
    // war.addPackage(prefix);
    war.addPackages(false, new MyFilter(), prefix);
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        addAllPackages(war, prefix + "." + file.getName(), file);
      }
    }
  }

  /**
   * Add all resources from the given directory, recursively. Only adds subdirectories when they start with a lower case
   * letter
   *
   * @param war
   *          war archive to add packages to
   * @param directory
   *          directory with resources to add
   */
  protected static void addAllResources(WebArchive war, String directory) {
    for (File file : new File(directory).listFiles()) {
      // pathname.pathname.isFile() || Character.isLowerCase(pathname.getName().charAt(0))
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
      LOGGER.fine("Ressource add:" + prefix + dir.getName());
      war.addAsResource(dir, prefix + dir.getName());
    }
  }

}
