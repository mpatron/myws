package org.jobjects.myws.tools.log;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Connecteur JAAS.
 * 
 * @author Mickaël Patron 28/02/2015
 */
public class JObjectsLogFormatter extends Formatter {
  private static final DateFormat format = new SimpleDateFormat("h:mm:ss");

  @Override
  public String format(LogRecord record) {
    String loggerName = record.getLoggerName();
    if (loggerName == null) {
      loggerName = "root";
    }
    StringBuilder output = new StringBuilder()
        .append("[")
        .append(record.getLevel())
        .append(']')
        // .append(Thread.currentThread().getName()).append('|')
        .append(" " + format.format(new Date(record.getMillis())))
        .append(" : ")
        .append("..."+StringUtils.substringAfterLast(record.getSourceClassName(), "org.jobjects.") +"."+ record.getSourceMethodName()+"()")
        .append(" : ");

    if (record.getParameters() != null) {
      output.append(MessageFormat.format(record.getMessage(), record.getParameters()));
    } else {
      output.append(record.getMessage());
    }

    if (record.getThrown() != null) {
      output.append(SystemUtils.LINE_SEPARATOR);
      output.append(ExceptionUtils.getStackTrace(record.getThrown()));
    }

    output.append(SystemUtils.LINE_SEPARATOR);
    return output.toString();
  }

}
