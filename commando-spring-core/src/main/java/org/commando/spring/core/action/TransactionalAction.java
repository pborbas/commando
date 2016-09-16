package org.commando.spring.core.action;

import org.commando.exception.DispatchException;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Created by pborbas on 01/07/15.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional(rollbackFor = {RuntimeException.class, DispatchException.class})
public @interface TransactionalAction {

	@AliasFor(annotation = Transactional.class)
	Isolation isolation() default Isolation.READ_COMMITTED;

	@AliasFor(annotation = Transactional.class)
	Propagation propagation() default Propagation.REQUIRED;

	@AliasFor(annotation = Transactional.class)
	boolean readOnly() default false;
}
