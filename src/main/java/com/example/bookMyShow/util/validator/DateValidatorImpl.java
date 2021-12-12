package com.example.bookMyShow.util.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidatorImpl implements ConstraintValidator<DateValidator, String>
{
	String format, message, regex;

	@Override
	public void initialize(DateValidator constraintAnnotation)
	{
		this.format = constraintAnnotation.format();
		this.message = constraintAnnotation.message();
		this.regex = constraintAnnotation.regex();
	}

	public boolean isValid(String value, ConstraintValidatorContext context)
	{
		context.disableDefaultConstraintViolation();
		DateFormat df = new SimpleDateFormat(format);
		this.customMessageForValidation(context, message + ".Date Must follow format " + format);
		if(value == null)
		{
			return true;
		}
		if(!Pattern.matches(regex, value))
		{
			return false;
		}
		try
		{
			df.parse(value);
			return true;
		}
		catch(ParseException e)
		{
			return false;
		}
	}

	private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message)
	{
		constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

