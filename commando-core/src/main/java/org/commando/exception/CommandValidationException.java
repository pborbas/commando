package org.commando.exception;

import org.commando.command.Command;

import javax.naming.spi.DirStateFactory.Result;

/*
 * Validation exception that contains the error message, the reasons why it failed and the validated command 
 */
public class CommandValidationException extends ValidationException {

    private static final long serialVersionUID = 1L;
    private final Command<? extends Result> command;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CommandValidationException(Command command, String message, String... reasons) {
        super(message, reasons);
        this.command=command;
    }
    
    public Command<? extends Result> getCommand() {
        return command;
    }

}
