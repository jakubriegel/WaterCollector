package eu.jrie.crx.watercollector.domain.volume.calculator;

import eu.jrie.crx.watercollector.domain.volume.StripeCell;

import java.util.ArrayList;
import java.util.List;

record PartialResultWithDetails(
        List<List<StripeCell>> stripes,
        ArrayList<Integer> remainingSurface
) {}
