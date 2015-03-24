package org.commando.action;

import java.lang.reflect.ParameterizedType;

import org.commando.command.Command;
import org.commando.exception.CommandValidationException;
import org.commando.result.Result;

public abstract class AbstractAction<C extends Command<R>, R extends Result> implements Action<C, R> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<C> getCommandType() {
        return (Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Action<C, R> validate(final C listCustomersCommand) throws CommandValidationException {
        return this;
    }

}
