package org.commando.result;

/**
 * Use when your action doesn't have useful result like {@link VoidResult} but NoResult also means for async communication protocolls like JMS, to not wait for a result
 * 
 * @author borbasp
 * 
 */
public class NoResult extends AbstractResult implements Result {

	private NoResult() {
		//for serialization
	}

	public NoResult(final String commandId) {
        super(commandId);
    }


}
