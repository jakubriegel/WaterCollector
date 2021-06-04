package eu.jrie.crx.watercollector.api;

import eu.jrie.crx.watercollector.api.message.VolumeDetailsResponse;
import eu.jrie.crx.watercollector.api.message.VolumeResponse;
import eu.jrie.crx.watercollector.domain.volume.InvalidBarHeightException;
import eu.jrie.crx.watercollector.domain.volume.VolumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("water")
public class VolumeController {

    private final VolumeService service;

    public VolumeController(VolumeService service) {
        this.service = service;
    }

    @GetMapping(value = "volume", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VolumeResponse> getVolume(@RequestParam List<String> bars) throws InvalidBarHeightException {
        var surface = convertBarsToSurface(bars);
        final int volume = service.calculateVolume(surface);
        var response = new VolumeResponse(surface, volume);
        return ok(response);
    }

    @GetMapping(value = "volume/details", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<VolumeDetailsResponse> getVolumeDetails(@RequestParam List<String> bars) throws InvalidBarHeightException {
        var surface = convertBarsToSurface(bars);
        var details = service.calculateVolumeWithDetails(surface);
        var response = new VolumeDetailsResponse(
                details.surface(),
                details.volume(), details.barsVolume(), details.emptyVolume(),
                details.width(), details.height(),
                details.stripes()
        );
        return ok(response);
    }

    private static List<Integer> convertBarsToSurface(List<String> bars) {
        return bars.stream()
                .map(Integer::valueOf)
                .toList();
    }
}
