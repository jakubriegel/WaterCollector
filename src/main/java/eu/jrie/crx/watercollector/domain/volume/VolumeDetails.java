package eu.jrie.crx.watercollector.domain.volume;

import java.util.List;

public record VolumeDetails (
        List<Integer> surface,
        int volume,
        int barsVolume,
        int emptyVolume,
        int width,
        int height,
        List<List<StripeCell>> stripes
) {}
