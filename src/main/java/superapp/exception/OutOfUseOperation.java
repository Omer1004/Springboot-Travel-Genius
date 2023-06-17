package superapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class OutOfUseOperation extends RuntimeException {
	private static final long serialVersionUID = 6096058771043646466L;

	public OutOfUseOperation() {
	}

	public OutOfUseOperation(String message) {
		super(message);
	}

	public OutOfUseOperation(Throwable cause) {
		super(cause);
	}

	public OutOfUseOperation(String message, Throwable cause) {
		super(message, cause);
	}

}
