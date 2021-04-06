package io.capsule.filemanager.domain;

import org.springframework.http.HttpStatus;

public class APIError {
	private HttpStatus status;
	private String message;

	public APIError() { }

	public APIError(HttpStatus status) {
		this.status = status;
		this.message = "Technical Error Occured.";
	}

	public APIError(HttpStatus status, Throwable ex) {
		this.status = status;
		this.message = ex.getMessage();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
