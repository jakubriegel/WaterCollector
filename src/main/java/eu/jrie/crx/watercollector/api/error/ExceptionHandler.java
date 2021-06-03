package eu.jrie.crx.watercollector.api.error;

import eu.jrie.crx.watercollector.api.error.message.MissingParameterResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Collections.singletonList;
import static org.springframework.http.ResponseEntity.badRequest;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var response = new MissingParameterResponse(
                ((ServletWebRequest)request).getRequest().getRequestURI(),
                "The request is missing required parameters.",
                singletonList("bars")
        );
        return badRequest().body(response);
    }
}
