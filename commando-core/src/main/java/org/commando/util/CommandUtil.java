package org.commando.util;

import java.lang.reflect.ParameterizedType;

import org.commando.command.Command;
import org.commando.result.NoResult;
import org.commando.result.Result;

public class CommandUtil {

    @SuppressWarnings("unchecked")
    public static Class<? extends Result> getResultType(final Command<? extends Result> command) {
        return (Class<? extends Result>) ((ParameterizedType) command.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static boolean isNoResultCommand(final Command<? extends Result> command) {
        Class<? extends Result> resultClass=getResultType(command);
        return NoResult.class.isAssignableFrom(resultClass);
    }

}
