package com.arrow.apollo.web;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.Ordered;

import com.arrow.acs.AcsErrorResponse;
import com.arrow.apollo.web.exception.NotFoundException;
import com.arrow.pegasus.CoreWebExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler extends CoreWebExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<AcsErrorResponse> handleNotFoundException(NotFoundException e) {
		return buildResponse(HttpStatus.NOT_FOUND, e);
	}

}
