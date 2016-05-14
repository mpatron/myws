package org.jobjects.myws.tools.wildfly;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author Mickaël Patron 13/09/2015
 */
public class ProcessBuilderMultipleCommandsExample {
  private final static Logger LOGGER = Logger.getLogger(ProcessBuilderMultipleCommandsExample.class.getName());

  public static void main(String[] args) throws InterruptedException, IOException {
    // multiple commands
    // /C Carries out the command specified by string and then terminates
    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C dir & echo example of & echo working dir");
    // pb.directory(new File("src"));
    pb.directory(new File(System.getProperty("user.dir")));

    LOGGER.info(pb.directory().getAbsolutePath());

    Process process = pb.start();
    IOThreadHandler outputHandler = new IOThreadHandler(process.getInputStream());
    outputHandler.start();
    process.waitFor();
    LOGGER.info(outputHandler.getOutput().toString());
    LOGGER.info("fin.");
  }

  private static class IOThreadHandler extends Thread {
    private InputStream inputStream;
    private StringBuilder output = new StringBuilder();

    IOThreadHandler(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    public void run() {
      Scanner br = null;
      try {
        br = new Scanner(new InputStreamReader(inputStream));
        String line = null;
        while (br.hasNextLine()) {
          line = br.nextLine();
          output.append(line + System.lineSeparator());
        }
      } finally {
        br.close();
      }
    }

    public StringBuilder getOutput() {
      return output;
    }
  }
}