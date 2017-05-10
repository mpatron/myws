package org.jobjects.myws.tools.wildfly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.dmr.ModelNode;

/**
 * Connecteur JAAS.
 * 
 * @author Mickaël Patron 28/02/2015
 */
public class CliUtils {
  private final static Logger LOGGER = Logger.getLogger(CliUtils.class.getName());
  private static final String K_RESULT = "result";
  private static final String FILE_PREFIX = "jboss-cli-rest";
  private static final String FILE_SUFFIX = ".cli";

  private static final String V_SUCCESS = "success";
  private static final String K_OUTCOME = "outcome";

  private static File createFile(String[] cmd) throws Exception {
    File file;
    try {
      file = File.createTempFile(FILE_PREFIX, FILE_SUFFIX);
    } catch (IOException e) {
      throw new Exception("Failed to create cli script: " + e.getLocalizedMessage());
    }

    FileWriter writer = null;
    try {
      writer = new FileWriter(file);
      for (String line : cmd) {
        writer.write(line);
        writer.write('\n');
      }
    } catch (IOException e) {
      throw new Exception("Failed to write to " + file.getAbsolutePath() + ": " + e.getLocalizedMessage());
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
        }
      }
    }

    return file;
  }

  public static String execute(String cmd, boolean logFailure) throws Exception {
    return execute(new String[] { cmd }, logFailure);
  }

  public static String execute(String[] cmd, boolean logFailure) throws Exception {
    return execute(createFile(cmd), logFailure);
  }

  public static String execute(File f, boolean logFailure) throws Exception {
    String returnValue = null;
    final String jbossDist = System.getenv("JBOSS_HOME");
    // String jbossDist = "C:\\programs\\wildfly-9.0.1.Final";
    if (jbossDist == null) {
      LOGGER.severe("JBOSS_HOME env property is not set");
      throw new Exception("JBOSS_HOME env property is not set");
    }

    if (!f.exists()) {
      LOGGER.severe("File " + f.getAbsolutePath() + " doesn't exist");
      throw new Exception("File " + f.getAbsolutePath() + " doesn't exist");
    }

    final List<String> command = new ArrayList<>();
    command.add("java");
    command.add("-Djava.net.preferIPv4Stack=true");
    command.add("-Djava.net.preferIPv6Addresses=false");
    command.add("-jar");
    command.add(jbossDist + File.separatorChar + "jboss-modules.jar");
    command.add("-mp");
    command.add(jbossDist + File.separatorChar + "modules");
    command.add("org.jboss.as.cli");
    command.add("-c");
    command.add("--controller=" + "localhost" + ":" + "9990");
    command.add("--file=" + f.getAbsolutePath());
    // builder.redirectErrorStream(true);
    final ProcessBuilder builder = new ProcessBuilder(command);
    Process cliProc = null;
    try {
      builder.inheritIO();
      builder.redirectErrorStream(true);
      cliProc = builder.start();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      throw new Exception("Failed to start CLI process: " + e.getLocalizedMessage(), e);
    }

    try {
      IOThreadHandler outputHandler = new IOThreadHandler(cliProc.getInputStream());
      outputHandler.start();
      // IOThreadHandler errorHandler = new IOThreadHandler(cliProc.getErrorStream());
      // errorHandler.start();

      int exitCode = cliProc.waitFor();
      returnValue = outputHandler.getOutput().toString();
      if (exitCode != 0) {
        // returnValue += System.lineSeparator() + errorHandler.getOutput().toString();
        LOGGER.severe("Erreur du JBoss-cli exitCode=" + exitCode + System.lineSeparator() + returnValue);
      }
      LOGGER.info("Chargement de " + f.getAbsolutePath());
    } catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      throw new Exception("Interrupted waiting for the CLI process.", e);
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e1) {
      LOGGER.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
      throw new Exception("Thread de 1500 milliseconde", e1);
    }

    // String output = null;
    // try {
    // int bytesTotal = cliProc.getInputStream().available();
    // if (bytesTotal > 0) {
    // final byte[] bytes = new byte[bytesTotal];
    // cliProc.getInputStream().read(bytes);
    // output = new String(bytes);
    // }
    //
    // bytesTotal = cliProc.getErrorStream().available();
    // if (bytesTotal > 0) {
    // final byte[] bytes = new byte[bytesTotal];
    // cliProc.getErrorStream().read(bytes);
    // LOGGER.severe( new String(bytes));
    // }
    // returnValue=output;
    // } catch (Exception e) {
    // throw new Exception("Failed to read command's output: " + e.getLocalizedMessage());
    // }

    return returnValue;
  }

  private static class IOThreadHandler extends Thread {
    private InputStream inputStream;
    private StringBuilder output = new StringBuilder();

    IOThreadHandler(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    public void run() {
      //Scanner br = null;
      try (Scanner br= new Scanner(new InputStreamReader(inputStream))) {
        //br = new Scanner(new InputStreamReader(inputStream));
        String line = null;
        while (br.hasNextLine()) {
          line = br.nextLine();
          output.append(line + System.lineSeparator());
        }
//      } finally {
//        br.close();
      }
    }

    public StringBuilder getOutput() {
      return output;
    }
  }

  public static boolean isSuccess(String output) {
    if (output == null) {
      return false;
    }

    ModelNode node = ModelNode.fromString(output);

    if (node != null && node.get(K_OUTCOME) != null) {
      return V_SUCCESS.equals(stripQuotes(node.get(K_OUTCOME).toString()));
    }

    return false;
  }

  public static ModelNode getResultObject(String output) {
    return ModelNode.fromString(output).get(K_RESULT);
  }

  public static String stripQuotes(String str) {
    return str == null ? null : str.replaceAll("^\"(.*)\"$", "$1");
  }

  public static void main(String[] args) throws Exception {
    LOGGER.info("CliUtils => " + CliUtils.execute(new File("src/rest/resources/jboss-montest-login-module.cli"), true));
  }
}
