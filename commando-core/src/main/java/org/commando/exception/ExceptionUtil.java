package org.commando.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExceptionUtil {

    private static final Log LOG = LogFactory.getLog(ExceptionUtil.class);
    private static final int MAX_STACKTRACE_LEVEL = 10;

    public static DispatchException findDispatchException(final Throwable throwable, int level) {
	if (level < MAX_STACKTRACE_LEVEL && throwable.getCause() != null) {
	    if (throwable.getCause() instanceof DispatchException) {
		return (DispatchException) throwable.getCause();
	    } else {
		return findDispatchException(throwable.getCause(), ++level);
	    }
	}
	return null;
    }

}
