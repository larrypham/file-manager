package io.capsule.filemanager.error.handler;

import io.capsule.filemanager.domain.APIError;
import io.capsule.filemanager.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<APIError> handleEntityNotFound(ResourceNotFoundException ex) {
		final APIError apiError = new APIError(HttpStatus.NOT_FOUND, ex);
		return new ResponseEntity<APIError>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<APIError> handleEntityNotFound(RuntimeException ex) {
		final APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
		return new ResponseEntity<APIError>(apiError, apiError.getStatus());
	}
}
