package com.arrow.pegasus;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arrow.acs.AcsErrorResponse;
import com.arrow.acs.AcsException;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.Loggable;

@ControllerAdvice
public class CoreWebExceptionHandler extends Loggable {

	public CoreWebExceptionHandler() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<AcsErrorResponse> handleInvalidLoginException(InvalidLoginException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(NotAuthorizedException.class)
	public ResponseEntity<AcsErrorResponse> handleNotAuthorizedException(NotAuthorizedException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(LoginRequiredException.class)
	public ResponseEntity<AcsErrorResponse> handleLoginRequiredException(LoginRequiredException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(UnverifiedAccountException.class)
	public ResponseEntity<AcsErrorResponse> handleUnverifiedAccountException(UnverifiedAccountException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(RequiredChangePasswordException.class)
	public ResponseEntity<AcsErrorResponse> handleRequiredChangePasswordException(RequiredChangePasswordException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(LockedAccountException.class)
	public ResponseEntity<AcsErrorResponse> handleLockedAccountException(LockedAccountException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(InActiveAccountException.class)
	public ResponseEntity<AcsErrorResponse> handleInactiveAccountException(InActiveAccountException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<AcsErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		return buildResponse(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(AcsException.class)
	public ResponseEntity<AcsErrorResponse> handlePegasusException(AcsException e) throws IOException {
		return buildResponse(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(AcsLogicalException.class)
	public ResponseEntity<AcsErrorResponse> handlePegasusLogicalException(AcsLogicalException e) throws IOException {
		return buildResponse(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(AcsSystemException.class)
	public ResponseEntity<AcsErrorResponse> handlePegasusSystemException(AcsSystemException e) throws IOException {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<AcsErrorResponse> handleThrowable(Throwable e) throws IOException {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<AcsErrorResponse> handleThrowable(AccessDeniedException e) throws IOException {
		return buildResponse(HttpStatus.FORBIDDEN, e);
	}

	protected ResponseEntity<AcsErrorResponse> buildResponse(HttpStatus status, Throwable e) {
		String method = "buildResponse";
		logError(method, ExceptionUtils.getStackTrace(e));
		return new ResponseEntity<AcsErrorResponse>(new AcsErrorResponse().withStatus(status.value())
		        .withMessage(e.getMessage()).withExceptionClassName(e.getClass().getName()), status);
	}
}
