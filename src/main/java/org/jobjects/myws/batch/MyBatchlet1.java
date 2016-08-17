package org.jobjects.myws.batch;

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

@Named
public class MyBatchlet1 extends AbstractBatchlet {
	private transient Logger LOGGER = Logger.getLogger(getClass().getName());
	
	public MyBatchlet1() {
		LOGGER.info("new MyBatchlet1");
	}

	@Override
	public String process() throws Exception {
		LOGGER.info("process()");
		return null;
	}

}
