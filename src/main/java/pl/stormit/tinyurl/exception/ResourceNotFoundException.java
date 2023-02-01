package pl.stormit.tinyurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value not found")
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
