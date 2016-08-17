package org.jobjects.myws.batch;

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

@Named("MyBachlet2")
public class MyBachlet2 extends AbstractBatchlet {
	private transient Logger LOGGER = Logger.getLogger(getClass().getName());
	
	public MyBachlet2() {
		LOGGER.info("new MyBatchlet1");
	}

	@Override
	public String process() throws Exception {
		LOGGER.info("process()");
		return null;
	}

}
