package org.commando.remote.http.receiver;

import org.commando.remote.receiver.CommandReceiver;

/**
 *
 */
public interface HttpCommandReceiver extends CommandReceiver {

	String getMappingUrl();
	String getServletName();

}
