package superapp.exception;




import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFound extends RuntimeException {
	private static final long serialVersionUID = 6096058771043646466L;

	public NotFound() {
	}

	public NotFound(String message) {
		super(message);
	}

	public NotFound(Throwable cause) {
		super(cause);
	}

	public NotFound(String message, Throwable cause) {
		super(message, cause);
	}

}
