package org.apache.logging.log4j.core.config.plugins.validation.constraints;

import org.apache.logging.log4j.core.config.plugins.validation.validators.ValidHostValidator;
import org.apache.logging.log4j.core.config.plugins.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(ValidHostValidator.class)
public @interface ValidHost {
    String message() default "The hostname is invalid";
}
