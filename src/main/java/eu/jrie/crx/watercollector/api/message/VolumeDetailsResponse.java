package eu.jrie.crx.watercollector.api.message;

import eu.jrie.crx.watercollector.domain.volume.StripeCell;

import java.util.List;

public record VolumeDetailsResponse(
        List<Integer> surface,
        int volume,
        int barsVolume,
        int emptyVolume,
        int width,
        int height,
        List<List<StripeCell>> stripes
) {}
