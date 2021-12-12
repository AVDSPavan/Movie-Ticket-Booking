package com.example.bookMyShow.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorResponse
{
	int code;
	Boolean success;
	String message;
	String details;
	Date timestamp;

	@Override
	public String toString()
	{
		return "ErrorResponse{" +
			"code=" + code +
			", success=" + success +
			", message='" + message + '\'' +
			", details='" + details + '\'' +
			", timestamp=" + timestamp +
			'}';
	}
}
