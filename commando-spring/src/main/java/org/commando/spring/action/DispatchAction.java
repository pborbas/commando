package org.commando.spring.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.commando.action.Action;
import org.commando.dispatcher.InVmDispatcher;
import org.springframework.stereotype.Component;

/**
 * Marks an action class as a {@link Action} component. Marked DispatchAction
 * components are automatically detected by springs component scan and are
 * injected into the {@link InVmDispatcher}
 * 
 * @author borbasp
 * 
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DispatchAction {

}
