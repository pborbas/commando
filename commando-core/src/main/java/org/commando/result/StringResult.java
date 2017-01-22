package org.commando.result;

public class StringResult extends AbstractSimpleResult<String> {
	protected StringResult() {
		//for serialization
	}

	public StringResult(String commandId, String value) {
		super(commandId, value);
	}

}
