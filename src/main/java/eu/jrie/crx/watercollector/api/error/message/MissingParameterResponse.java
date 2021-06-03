package eu.jrie.crx.watercollector.api.error.message;

import java.util.List;

public record MissingParameterResponse (
        String path,
        String message,
        List<String> requiredParameters
) implements ErrorResponse {}
