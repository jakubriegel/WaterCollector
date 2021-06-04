package eu.jrie.crx.watercollector.domain.volume.calculator;

import eu.jrie.crx.watercollector.domain.volume.StripeCell;
import eu.jrie.crx.watercollector.domain.volume.VolumeDetails;
import eu.jrie.crx.watercollector.domain.volume.offset.Offset;
import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static eu.jrie.crx.watercollector.domain.volume.StripeCell.BAR;
import static eu.jrie.crx.watercollector.domain.volume.StripeCell.EMPTY;
import static eu.jrie.crx.watercollector.domain.volume.StripeCell.WATER;
import static java.lang.Math.min;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class VolumeDetailsCalculator {
    private final OffsetRetriever offsetRetriever;

    private final ArrayList<Integer> surface;
    private final int width;
    private final int height;

    private int waterVolume = 0;
    private int barsVolume = 0;
    private int emptyVolume = 0;
    private final List<List<StripeCell>> stripes = new LinkedList<>();

    VolumeDetailsCalculator(OffsetRetriever offsetRetriever, ArrayList<Integer> surface) {
        this.offsetRetriever = offsetRetriever;
        this.surface = surface;
        this.width = surface.size();
        this.height = surface.stream()
                .max(Integer::compareTo)
                .orElse(0);
    }

    public VolumeDetails emptyDetails() {
        for (int bar : surface) {
            var stripe = buildStripe(bar, 0, height - bar);
            stripes.add(stripe);
        }
        return new VolumeDetails(
                surface,
                0, barsVolume, emptyVolume,
                width, height,
                stripes
        );
    }

    public VolumeDetails calculate() {
        var offset = offsetRetriever.retrieveOffset(surface);
        processLeftOffset(offset.left());

        var surfaceWithCandidates = prepareSurfaceWithCandidates(offset);
        if (surfaceWithCandidates.size() == 1) {
            final int bar = surfaceWithCandidates.get(0);
            var stripe = buildStripe(bar, 0, height - bar);
            stripes.add(stripe);
        } else if (!surfaceWithCandidates.isEmpty()) {
            var partialResult = new PartialResultWithDetails(emptyList(), surfaceWithCandidates);
            do {
                partialResult = calculateContainerVolumeWithDetails(partialResult.remainingSurface());
                stripes.addAll(partialResult.stripes());
            } while (!partialResult.remainingSurface().isEmpty());
        }

        processRightOffset(offset.right());
        return new VolumeDetails(
                surface,
                waterVolume, barsVolume,emptyVolume,
                width, height,
                unmodifiableList(stripes)
        );
    }

    private ArrayList<Integer> prepareSurfaceWithCandidates(Offset offset) {
        final int beginIndex = offset.left();
        final int endIndex = width - offset.right();
        return  new ArrayList<>(surface.subList(beginIndex, endIndex));
    }

    private void processLeftOffset(final int leftOffset) {
        for (int i = 0; i < leftOffset; i++) {
            var bar = surface.get(i);
            var stripe = buildStripe(bar, 0, height - bar);
            stripes.add(stripe);
        }
    }

    private void processRightOffset(final int rightOffset) {
        for (int i = rightOffset; i >= 1; i--) {
            final int bar = surface.get(surface.size()-i);
            var stripe = buildStripe(bar, 0, height - bar);
            stripes.add(stripe);
        }
    }

    private PartialResultWithDetails calculateContainerVolumeWithDetails(ArrayList<Integer> surfaceWithCandidates) {
        final int firstBar = surfaceWithCandidates.get(0);
        int lastBar = -1;
        int containerWidth = 1;
        final int remainingWidth = surfaceWithCandidates.size();
        for (; containerWidth < remainingWidth; containerWidth++) {
            lastBar = surfaceWithCandidates.get(containerWidth);
            if (containerHasEnded(lastBar, firstBar)) {
                break;
            }
        }

        final int localHeight = min(lastBar, firstBar);
        var stripes = cool(localHeight, containerWidth, surfaceWithCandidates);
        var remainingSurface = surfaceWithCandidates.subList(containerWidth, surfaceWithCandidates.size());
        return new PartialResultWithDetails(stripes, new ArrayList<>(remainingSurface));
    }

    private static boolean containerHasEnded(final int bar, final int firstBar) {
        return bar >= firstBar;
    }

    private List<List<StripeCell>> cool(final int localHeight, final int containerWidth, List<Integer> surfaceWithCandidates) {
        return surfaceWithCandidates.subList(0, containerWidth)
                .stream()
                .map(bar -> {
                    final int waterVolume = localHeight - bar;
                    final int emptyVolume = bar == 0 ? height - waterVolume : height - (bar + waterVolume);
                    return buildStripe(bar, waterVolume, emptyVolume);
                })
                .toList();
    }

    private List<StripeCell> buildStripe(final int barCells, final int waterCells, final int emptyCells) {
        var stripe = new LinkedList<StripeCell>();
        for (int i = 0; i < barCells; i++) {
            barsVolume++;
            stripe.addFirst(BAR);
        }
        for (int i = 0; i < waterCells; i++) {
            waterVolume++;
            stripe.addFirst(WATER);
        }
        for (int i = 0; i < emptyCells && stripe.size() < height; i++) {
            emptyVolume++;
            stripe.addFirst(EMPTY);
        }
        return unmodifiableList(stripe);
    }
}
