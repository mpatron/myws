package org.jobjects.myws.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.input.TeeInputStream;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class CustomRequestWrapperFilter implements ContainerRequestFilter {

    public final static String ENTITY_STREAM_COPY = "ENTITY_STREAM_COPY";

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        ByteArrayOutputStream proxyOutputStream = new ByteArrayOutputStream();
        requestContext.setEntityStream(new TeeInputStream(requestContext.getEntityStream(), proxyOutputStream));
        requestContext.setProperty(ENTITY_STREAM_COPY, proxyOutputStream.toString("UTF-8"));
    }

}
