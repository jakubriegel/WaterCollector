package eu.jrie.crx.watercollector.domain.volume.calculator;

import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class VolumeCalculatorFactory {

    private final OffsetRetriever offsetRetriever;

    public VolumeCalculatorFactory(OffsetRetriever offsetRetriever) {
        this.offsetRetriever = offsetRetriever;
    }

    public VolumeCalculator create(ArrayList<Integer> surface) {
        return new VolumeCalculator(offsetRetriever, surface);
    }
}
