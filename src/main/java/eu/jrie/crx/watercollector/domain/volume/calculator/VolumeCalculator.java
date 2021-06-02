package eu.jrie.crx.watercollector.domain.volume.calculator;

import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class VolumeCalculator {

    private final OffsetRetriever offsetRetriever;

    private final ArrayList<Integer> surface;
    private final int width;
    private final int height;

    private int volume;

    VolumeCalculator(OffsetRetriever offsetRetriever, ArrayList<Integer> surface) {
        this.offsetRetriever = offsetRetriever;
        this.surface = surface;
        this.width = surface.size();
        this.height = surface.stream()
                .max(Integer::compareTo)
                .orElseThrow();
        this.volume = width * height;
    }

    public int calculate() {
        var offset = offsetRetriever.retrieveOffset(surface);
        volume -= height * (offset.left() + offset.right());

        final int beginIndex = offset.left();
        final int endIndex = width - offset.right();
        var surfaceWithCandidates = new ArrayList<>(surface.subList(beginIndex, endIndex));

        if (surfaceWithCandidates.size() == 1) {
            volume -= height;
        } else if (!surfaceWithCandidates.isEmpty()) {
            PartialResult partialResult = new PartialResult(-1, surfaceWithCandidates);
            do {
                partialResult = calculateContainerVolume(partialResult.remainingSurface());
                volume -= partialResult.barsVolume();
            } while (!partialResult.remainingSurface().isEmpty());
        }
        return volume;
    }

    private PartialResult calculateContainerVolume(ArrayList<Integer> surfaceWithCandidates) {
        final int firstBar = surfaceWithCandidates.get(0);
        int lastBar = -1;
        int containerWidth = 1;
        for (; containerWidth < surfaceWithCandidates.size(); containerWidth++) {
            lastBar = surfaceWithCandidates.get(containerWidth);
            if (containerHasEnded(lastBar, firstBar)) {
                break;
            }
        }

        final int localHeight = min(lastBar, firstBar);
        final int barsVolume = calculateBarsVolume(localHeight, containerWidth, surfaceWithCandidates);
        var remainingSurface = surfaceWithCandidates.subList(containerWidth, surfaceWithCandidates.size());
        return new PartialResult(barsVolume, new ArrayList<>(remainingSurface));
    }

    private static boolean containerHasEnded(final int bar, final int firstBar) {
        return bar >= firstBar;
    }

    private int calculateBarsVolume(final int localHeight, final int containerWidth, List<Integer> surfaceWithCandidates) {
        return surfaceWithCandidates.subList(0, containerWidth)
                .stream()
                .map(bar -> bar == height ? bar : bar  + (height - localHeight))
                .reduce(Integer::sum)
                .orElse(0);
    }
}
