package org.commando.spring.core.dispatcher.security;

import org.commando.dispatcher.security.SecurityContextManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by pborbas on 22/03/16.
 */
public class SpringSecurityContextManager implements SecurityContextManager<Authentication>{
	@Override
	public Authentication getSecurityContext() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context!=null) {
			return context.getAuthentication();
		}
		return null;
	}

	@Override
	public void setSecurityContext(Authentication authentication) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context!=null && authentication!=null) {
			context.setAuthentication(authentication);
		}
	}

	@Override
	public void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
}
