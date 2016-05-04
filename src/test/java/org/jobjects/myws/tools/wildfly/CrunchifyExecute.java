package org.jobjects.myws.tools.wildfly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * http://crunchify.com/how-to-execute-tcpdump-linux-command-using-java-process-class-and-capture-tcpip-packets/
 * 
 * @author MPT
 *
 */
public class CrunchifyExecute {
  private final static Logger LOGGER = Logger.getLogger(CrunchifyExecute.class.getName());

  public static void main(String[] args) {
    String tcpDumpCmd = "echo \"This is ProcessBuilder Example from JCG\"";
    String tcpDumpResult = runTCPDUmp(tcpDumpCmd, true);
    LOGGER.info(tcpDumpResult);
  }

  public static String runTCPDUmp(String crunchifyCmd, boolean waitForResult) {
    LOGGER.info("inside runTCPDUmp()");
    String tcpdumpCmdResponse = "";
    ProcessBuilder crunchifyProcessBuilder = null;

    // Find OS running on VM
    String operatingSystem = System.getProperty("os.name");

    if (operatingSystem.toLowerCase().contains("window")) {
      // In case of windows run command using "crunchifyCmd"
      crunchifyProcessBuilder = new ProcessBuilder("cmd", "/c", crunchifyCmd);
    } else {
      // In case of Linux/Ubuntu run command using /bin/bash
      crunchifyProcessBuilder = new ProcessBuilder("/bin/bash", "-c", crunchifyCmd);
    }

    crunchifyProcessBuilder.redirectErrorStream(true);

    try {
      Process process = crunchifyProcessBuilder.start();
      if (waitForResult) {
        InputStream crunchifyStream = process.getInputStream();
        tcpdumpCmdResponse = getStringFromStream(crunchifyStream);
        crunchifyStream.close();
      }

    } catch (Exception e) {
      LOGGER.info("Error Executing tcpdump command" + e);
    }
    return tcpdumpCmdResponse;
  }

  private static String getStringFromStream(InputStream crunchifyStream) throws IOException {
    String returnValue = null;
    LOGGER.info("inside getStringFromStream()");
    if (crunchifyStream != null) {
      Writer crunchifyWriter = new StringWriter();

      char[] crunchifyBuffer = new char[2048];
      try {
        Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
        int count;
        while ((count = crunchifyReader.read(crunchifyBuffer)) != -1) {
          crunchifyWriter.write(crunchifyBuffer, 0, count);
        }
        crunchifyReader.close();
      } finally {
        crunchifyStream.close();
      }
      returnValue = crunchifyWriter.toString();
      crunchifyWriter.close();
    } else {
      returnValue = "";
    }
    return returnValue;
  }
}
