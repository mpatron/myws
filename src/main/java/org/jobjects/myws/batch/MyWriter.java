package org.jobjects.myws.batch;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

@Named
public class MyWriter extends AbstractItemWriter {

	public MyWriter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeItems(List<Object> items) throws Exception {
		// TODO Auto-generated method stub

	}

}
