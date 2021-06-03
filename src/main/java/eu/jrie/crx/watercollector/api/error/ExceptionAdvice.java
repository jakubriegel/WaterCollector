package eu.jrie.crx.watercollector.api.error;

import eu.jrie.crx.watercollector.domain.volume.InvalidBarHeightException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.springframework.http.ResponseEntity.badRequest;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var response = new ErrorResponse(
                "MISSING_PARAMETER",
                getRequestPath(request),
                "The request is missing required parameters.",
                singletonMap("requiredParameters", singletonList("bars"))
        );
        return badRequest().body(response);
    }

    @ExceptionHandler(value = { InvalidBarHeightException.class })
    public ResponseEntity<ErrorResponse> handleInvalidBarHeightException(InvalidBarHeightException e, WebRequest request) {
        var response = new ErrorResponse(
                "INVALID_BAR_HEIGHT",
                getRequestPath(request),
                "Provided surface contains negative bar heights.",
                singletonMap("surface", e.getSurface())
        );
        return badRequest().body(response);
    }

    private static String getRequestPath(WebRequest webRequest) {
        var request = ((ServletWebRequest)webRequest).getRequest();
        return request.getRequestURI();
    }
}
