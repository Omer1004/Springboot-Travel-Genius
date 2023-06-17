package superapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UnAuthorized extends RuntimeException {
	private static final long serialVersionUID = 6096058771043646466L;

	public UnAuthorized() {
	}

	public UnAuthorized(String message) {
		super(message);
	}

	public UnAuthorized(Throwable cause) {
		super(cause);
	}

	public UnAuthorized(String message, Throwable cause) {
		super(message, cause);
	}

}
