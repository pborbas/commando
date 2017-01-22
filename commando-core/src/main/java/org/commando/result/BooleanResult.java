package org.commando.result;

public class BooleanResult extends AbstractSimpleResult<Boolean> {

	private BooleanResult() {
		//for serialization
	}

    public BooleanResult(final String commandId, final Boolean value) {
        super(commandId, value);
    }

}
