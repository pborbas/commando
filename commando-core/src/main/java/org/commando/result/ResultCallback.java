package org.commando.result;

import org.commando.exception.DispatchException;

public interface ResultCallback<T extends Result> {

    void onSuccess(T result);

    void onError(DispatchException dispatchException);

}
