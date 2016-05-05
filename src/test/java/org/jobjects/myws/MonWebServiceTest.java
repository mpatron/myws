package org.jobjects.myws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws.tools.arquillian.AbstractRemoteIT;
import org.jobjects.myws.tools.log.JObjectsLogFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * @author Mickaël PATRON 2014
 **/
@RunWith(Arquillian.class)
public class MonWebServiceTest extends AbstractRemoteIT {

  private static Logger LOGGER = Logger.getLogger(MonWebServiceTest.class.getName());

  @ArquillianResource
  private URL deployUrl;

  @Test
  public void testMafonction() throws IOException, SAXException {
    JObjectsLogFormatter.initializeLogging();
    LOGGER.info("deployUrl : " + (deployUrl==null?StringUtils.EMPTY:deployUrl.toString()));
    WebConversation webConversation = new WebConversation();
    InputStream source = new ByteArrayInputStream("rien".getBytes());
    PutMethodWebRequest request = new PutMethodWebRequest(deployUrl + "/api/lapp/mafonction?param1=pArAm_1&param2=PaRam_2", source, MediaType.APPLICATION_JSON);
    WebResponse response = webConversation.getResponse(request);
    assertEquals(200,response.getResponseCode());
    LOGGER.info(response.getText());
    assertTrue(response.getText().contains("pArAm_1"));
    assertTrue(response.getText().contains("PaRam_2"));
    source.close();
  }  
  
  @Test
  public void testMafonction2() throws IOException, SAXException {
    WebConversation webConversation = new WebConversation();
    InputStream source = new ByteArrayInputStream("rien".getBytes());
    GetMethodWebRequest request = new GetMethodWebRequest(deployUrl + "/api/lapp/mafonction2");
    WebResponse response = webConversation.getResponse(request);
    assertEquals(200,response.getResponseCode());
    LOGGER.info("Json :" + response.getText());
    assertTrue(response.getText().contains("true"));
    source.close();
  }  
}
