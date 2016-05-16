package org.jobjects.myws.tools.log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Logger. Logger LOGGER =
 * Logger.getLogger(JObjectsLogFormatter.class.getName());
 * LOGGER.info("java.util.logging.config.file="+System.getProperty(
 * "java.util.logging.config.file"));
 * 
 * @author Mickaël Patron 28/02/2015
 */
public class JObjectsLogFormatter extends Formatter {
  private static final DateFormat format = new SimpleDateFormat("h:mm:ss");

  /**
   * Attention, il faut rendre la methode appelable 1 fois.
   */
  public static void initializeLogging() {
    final String filePathnameLogging = "org/jobjects/logging.properties";
    try {
      URL url = ClassLoader.getSystemResource(filePathnameLogging);
      if (url != null) {
        Path path = Paths.get(url.toURI());
        if (Files.isReadable(path)) {
          InputStream is = new FileInputStream(path.toAbsolutePath().toString());
          LogManager.getLogManager().readConfiguration(is);
          is.close();
        } else {
          System.err.println("Le fichier logging.properties est illisible : " + path.toAbsolutePath());
        }
      } else {
        System.err.println("Le chemin d'accès à logging.properties est introuvable : " + filePathnameLogging);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String format(LogRecord record) {
    String loggerName = record.getLoggerName();
    if (loggerName == null) {
      loggerName = "root";
    }
    StringBuilder output = new StringBuilder().append("[").append(record.getLevel()).append(']')
        // .append(Thread.currentThread().getName()).append('|')
        .append(" " + format.format(new Date(record.getMillis()))).append(" : ")
        .append("..." + StringUtils.substringAfterLast(record.getSourceClassName(), "org.jobjects.") + "." + record.getSourceMethodName() + "()").append(" : ");

    if (record.getParameters() != null) {
      output.append(MessageFormat.format(record.getMessage(), record.getParameters()));
    } else {
      output.append(record.getMessage());
    }

    if (record.getThrown() != null) {
      output.append(System.lineSeparator());
      output.append(ExceptionUtils.getStackTrace(record.getThrown()));
    }

    output.append(System.lineSeparator());
    return output.toString();
  }

}
