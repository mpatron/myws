package org.jobjects.myws.file;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * https://www.mkyong.com/java/java-sha-hashing-example/
 * @author Mickael
 *
 */
public class SHACheckSumExample {

  public static void main(String[] args) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(args[0]);// "c:\\loging.log"

    byte[] dataBytes = new byte[8192];

    int nread = 0;
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }
    fis.close();
    byte[] mdbytes = md.digest();

    // convert the byte to hex format method 1
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    System.out.println("Hex format : " + sb.toString());

    // convert the byte to hex format method 2
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
      // String.format("%02x",d);
    }

    System.out.println("Hex format : " + hexString.toString());

    StringBuffer sb2 = new StringBuffer();
    for (byte d : mdbytes) {
      sb2.append(String.format("%02x", d));
    }
    System.out.println("Hex format : " + sb2.toString());

  }

}
