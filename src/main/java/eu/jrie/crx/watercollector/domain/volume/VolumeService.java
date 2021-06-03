package eu.jrie.crx.watercollector.domain.volume;

import eu.jrie.crx.watercollector.domain.volume.calculator.VolumeCalculatorFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolumeService {

    private final VolumeCalculatorFactory volumeCalculatorFactory;

    public VolumeService(VolumeCalculatorFactory volumeCalculatorFactory) {
        this.volumeCalculatorFactory = volumeCalculatorFactory;
    }

    public int calculateVolume(List<Integer> surface) throws InvalidBarHeightException {
        verifyAllHeightsAreValid(surface);
        if (hasNoContainersCandidates(surface)) {
            return 0;
        } else {
            var calculator = volumeCalculatorFactory.create(surface);
            return calculator.calculate();
        }
    }

    private static boolean hasNoContainersCandidates(List<Integer> surface) {
        return surface.isEmpty() ||
                surface.size() < 3 ||
                surface.stream()
                    .distinct()
                    .count() == 1;
    }

    private static void verifyAllHeightsAreValid(List<Integer> surface) throws InvalidBarHeightException {
        var containsInvalid = surface.stream()
                .anyMatch(bar -> bar < 0);

        if (containsInvalid) {
            throw new InvalidBarHeightException(surface);
        }
    }
}
