package team.exlab.ecohub.news.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ContainersNumberValidator.class})
public @interface ContainersNumber {
	String message() default "The input list should contain between 3 and 20 keywords and their length should be less then 100";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
