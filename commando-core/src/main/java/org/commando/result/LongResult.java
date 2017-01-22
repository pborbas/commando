package org.commando.result;

public class LongResult extends AbstractSimpleResult<Long> {
	private LongResult() {
		//for serialization
	}

    public LongResult(String commandId, Long value) {
        super(commandId, value);
    }

}
