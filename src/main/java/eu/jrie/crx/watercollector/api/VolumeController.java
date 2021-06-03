package eu.jrie.crx.watercollector.api;

import eu.jrie.crx.watercollector.api.message.VolumeResponse;
import eu.jrie.crx.watercollector.domain.volume.VolumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.String.join;
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
    public ResponseEntity<VolumeResponse> getVolume(@RequestParam List<String> bars) {
        var surface = bars.stream()
                .map(Integer::valueOf)
                .toList();
        final int volume = service.calculateVolume(surface);
        return ok(new VolumeResponse(surface, volume));
    }

    @GetMapping(value = "volume/details", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVolumeDetails(@RequestParam List<String> values) {
        return ok(join(", ", values));
    }
}
