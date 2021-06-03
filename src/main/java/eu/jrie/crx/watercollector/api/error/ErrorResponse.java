package eu.jrie.crx.watercollector.api.error;

import java.util.Map;

public record ErrorResponse(
        String code,
        String path,
        String message,
        Map<String, Object> details
) {}
