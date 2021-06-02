package eu.jrie.crx.watercollector.domain.volume;

import eu.jrie.crx.watercollector.domain.volume.calculator.VolumeCalculatorFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VolumeService {

    private final VolumeCalculatorFactory volumeCalculatorFactory;

    public VolumeService(VolumeCalculatorFactory volumeCalculatorFactory) {
        this.volumeCalculatorFactory = volumeCalculatorFactory;
    }

    public int calculateVolume(ArrayList<Integer> surface) {
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
}
