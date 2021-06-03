package eu.jrie.crx.watercollector.api.message;

import java.util.List;

public record VolumeResponse(
        List<Integer> surface,
        int volume
) {}
