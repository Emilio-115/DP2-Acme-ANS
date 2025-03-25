
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import acme.internals.components.validators.StringValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValidator.class)
@ReportAsSingleViolation

public @interface ValidShortText {

	int min() default 0;
	int max() default 50;
	String pattern() default "";

	String message() default "{acme.validation.short-text.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
