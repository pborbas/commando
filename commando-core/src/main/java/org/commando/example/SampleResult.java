package org.commando.example;

import org.commando.result.AbstractSimpleResult;

public class SampleResult extends AbstractSimpleResult<String> {

	private SampleResult() {
		super("");
	}

	public SampleResult(final String commandId, String data) {
		super(commandId, data);
	}

}
