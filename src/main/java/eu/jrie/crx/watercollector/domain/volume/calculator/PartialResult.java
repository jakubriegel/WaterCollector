package eu.jrie.crx.watercollector.domain.volume.calculator;

import java.util.ArrayList;

record PartialResult(
        int barsVolume,
        ArrayList<Integer> remainingSurface
) {}
