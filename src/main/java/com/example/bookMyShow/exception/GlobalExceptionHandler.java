package com.example.bookMyShow.exception;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.bookMyShow.model.error.ErrorResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> handleEscrowException(ServiceException e, WebRequest request)
	{
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK.value(), false, e.getMessage(), request.getDescription(false), new Date());
		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception e, WebRequest request)
	{
		log.error("Error ", e);
		ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), false, String.format("REQUEST FAILED %s", e.getMessage()), request.getDescription(false), new Date());
		return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request)
	{
		String errorMsg = "";
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for(FieldError error : fieldErrors)
		{
			errorMsg += error.getDefaultMessage() + ", ";
		}

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), false, errorMsg, request.getDescription(false), new Date());
		log.error("Error: {}", errorResponse);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}