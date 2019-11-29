package org.commando.remote.exception;

import org.commando.exception.DispatchException;

import java.lang.reflect.Constructor;

/**
 *
 */
public class RemoteExceptionUtil {

	/**
	 * Turns a text message content into a DispatcherException
	 * @param message
	 * @param exceptionClassName
	 * @return
	 */
	public static DispatchException convertToException(String message, String exceptionClassName) {
		try {
			Class<?> exceptionClass = Class.forName(exceptionClassName);
			Constructor<?> cons = exceptionClass.getConstructor(String.class);
			Object instance = cons.newInstance(message);
			if (instance instanceof DispatchException) {
				return (DispatchException) instance;
			} else {
				return new DispatchException(message, (Throwable) instance);
			}
		} catch (Throwable e) {
			return new DispatchException(message, e);
		}
	}
}
