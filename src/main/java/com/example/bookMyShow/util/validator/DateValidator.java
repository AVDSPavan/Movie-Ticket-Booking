package com.example.bookMyShow.util.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
	validatedBy = {com.example.bookMyShow.util.validator.DateValidatorImpl.class}
)
public @interface DateValidator
{
	String message() default "Not a valid Date";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String format() default "yyyy-MM-dd hh:mm:ss";

	String regex() default "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
}
