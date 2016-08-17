package org.jobjects.myws.batch;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

public class BatchMain {

	public static void main(String[] args) {
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		Properties props = new Properties();
		jobOperator.start("myJob", props);
	}

}
