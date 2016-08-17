package org.jobjects.myws.batch;

import java.util.logging.Logger;

import javax.batch.api.listener.AbstractJobListener;
import javax.inject.Named;

@Named
public class MyJobListener extends AbstractJobListener {
	private transient Logger LOGGER = Logger.getLogger(getClass().getName());
	public MyJobListener() {
		LOGGER.info("new MyJobListener");
	}
	@Override
	public void beforeJob() throws Exception {
		LOGGER.info("beforeJob()");
	}
	@Override
	public void afterJob() throws Exception {
		LOGGER.info("afterJob()");
	}

}
