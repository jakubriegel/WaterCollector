package eu.jrie.crx.watercollector.domain.volume.calculator;

import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VolumeCalculatorFactory {

    private final OffsetRetriever offsetRetriever;

    public VolumeCalculatorFactory(OffsetRetriever offsetRetriever) {
        this.offsetRetriever = offsetRetriever;
    }

    public VolumeCalculator createCalculator(List<Integer> surface) {
        return new VolumeCalculator(offsetRetriever, new ArrayList<>(surface));
    }

    public VolumeDetailsCalculator createCalculatorWithDetails(List<Integer> surface) {
        return new VolumeDetailsCalculator(offsetRetriever, new ArrayList<>(surface));
    }
}
