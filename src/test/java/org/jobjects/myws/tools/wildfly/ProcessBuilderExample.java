package org.jobjects.myws.tools.wildfly;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * @author Mickaël Patron 13/09/2015
 */
public class ProcessBuilderExample {
  private final static Logger LOGGER = Logger.getLogger(ProcessBuilderExample.class.getName());

  public static void main(String[] args) throws InterruptedException, IOException {
    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo \"This is ProcessBuilder Example from JCG\"", "\n");
    pb.inheritIO();
    pb.redirectErrorStream(true);
    System.out.println("Run echo command");
    // pb.redirectError(Redirect.INHERIT);
    // pb.redirectOutput(Redirect.INHERIT);
    Process process = pb.start();

    LOGGER.info("before pipeOutput.");
    pipeOutput(process);
    LOGGER.info("after pipeOutput.");

    LOGGER.info("before waitFor.");
    int errCode = process.waitFor();
    LOGGER.info("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
    // System.out.println("Echo Output:\n" + output(process.getInputStream()));
    pipeClose(process);
    process = null;
    Runtime.getRuntime().gc();
  }

  private static void pipeOutput(Process process) {
    // pipe(process.getErrorStream(), System.err);
    pipe(process.getInputStream(), System.out);
    // pipein(System.in, process.getOutputStream());
  }

  private static void pipeClose(Process process) throws IOException {
    // process.getErrorStream().close();
    process.getInputStream().close();
    // process.getOutputStream().close();
  }

  private static void pipe(final InputStream src, final PrintStream dest) {
    new Thread(() -> {
      try {
        byte[] buffer = new byte[1024];
        for (int n = 0; n != -1; n = src.read(buffer)) {
          dest.write(buffer, 0, n);
        }
      } catch (IOException e) { // just exit
      }
    }).start();
  }

  // private static void pipein(final InputStream src, final OutputStream dest) {
  // new Thread(new Runnable() {
  // public void run() {
  // try {
  // int ret = -1;
  // while ((ret = System.in.read()) != -1) {
  // dest.write(ret);
  // dest.flush();
  // }
  // } catch (IOException e) { // just exit
  // }
  // }
  // }).start();
  // }

}