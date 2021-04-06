package io.capsule.filemanager.exception;

public class ResourceNotFoundException  extends RuntimeException {
	private static final long serialVersionUUID = 1L;
	private String message;

	public ResourceNotFoundException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
