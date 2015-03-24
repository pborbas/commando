package org.commando.remote;

import org.commando.remote.exception.DispatchUrlParsingException;
import org.commando.remote.model.DispatcherUrl;

/**
 * Parses an URL into {@link DispatcherUrl} object
 * 
 * @author pborbas
 *
 */
public interface DispatcherUrlParser {
    DispatcherUrl parse(String url) throws DispatchUrlParsingException;
}
