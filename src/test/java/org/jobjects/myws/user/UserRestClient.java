package org.jobjects.myws.user;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meterware.httpunit.DeleteMethodWebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class UserRestClient {
  private static Logger LOGGER = Logger.getLogger(UserRESTWebServiceTest.class.getName());

  private URL deployUrl;
  
  public UserRestClient(URL deployUrl) {
    this.deployUrl = deployUrl;
  }
  
  public User create(User user) {
    User returnValue = null;
    try {
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(deployUrl.toString().replace("8080", "9143") + "api/user");
      Response response = webTarget.request().post(Entity.json(user));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(User.class);
        LOGGER.finest("create -> userReturn=" + returnValue);
      } else {
        LOGGER.log(Level.SEVERE,
            statusType.getReasonPhrase() + " => " + (response.bufferEntity() ? response.readEntity(String.class) : StringUtils.EMPTY));
      }
      
    } catch (RuntimeException e) {
      LOGGER.log(Level.WARNING, e.getMessage() + " user = " + ReflectionToStringBuilder.toString(user, ToStringStyle.JSON_STYLE));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return returnValue;
  }

  public User update(User user) {
    User returnValue = null;
    try {
      PipedOutputStream pos = new PipedOutputStream(); 
      PipedInputStream pis = new PipedInputStream(pos);
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(Include.NON_NULL);
      mapper.writeValue(pos, user);
      
      PutMethodWebRequest request = new PutMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user", pis, MediaType.APPLICATION_JSON);
      request.setHeaderField(HttpHeaders.CONTENT_ENCODING, "UTF-8");
      WebConversation webConversation = new WebConversation();
      WebResponse response = webConversation.getResponse(request);
      if(200 ==  response.getResponseCode()) {        
        returnValue = mapper.readValue(response.getInputStream(), User.class);
      } 
      pos.close();
      pis.close();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return returnValue;
  }

  public User show(String email) {
    User returnValue = null;
    try {      
      GetMethodWebRequest request = new GetMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user/"+URLEncoder.encode(email, "UTF-8"));
      request.setHeaderField(HttpHeaders.CONTENT_ENCODING, "UTF-8");
      WebConversation webConversation = new WebConversation();
      WebResponse response = webConversation.getResponse(request);
      if(200 ==  response.getResponseCode()) {//Response.Status.Family.familyOf(statusCode)
        ObjectMapper mapper = new ObjectMapper();
        returnValue = mapper.readValue(response.getInputStream(), User.class);
      } 
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return returnValue;
  }

  public User delete(String email) {
    User returnValue = null;
    try {      
      DeleteMethodWebRequest request = new DeleteMethodWebRequest(deployUrl.toString().replace("8080", "9143") + "/api/user/"+URLEncoder.encode(email, "UTF-8"));
      request.setHeaderField(HttpHeaders.CONTENT_ENCODING, "UTF-8");
      WebConversation webConversation = new WebConversation();
      WebResponse response = webConversation.getResponse(request);
      if(200 ==  response.getResponseCode()) {
        ObjectMapper mapper = new ObjectMapper();
        returnValue = mapper.readValue(response.getInputStream(), User.class);
      } 
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    return returnValue;
  }
}
