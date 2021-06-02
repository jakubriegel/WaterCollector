package eu.jrie.crx.watercollector.api;

import eu.jrie.crx.watercollector.domain.volume.VolumeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;

@RestController
@RequestMapping("water")
public class VolumeController {

    private final VolumeService service;

    public VolumeController(VolumeService service) {
        this.service = service;
    }

    @GetMapping("volume")
    public String getVolume(@RequestParam ArrayList<String> values) {
        return join(", ", values);
    }

    @GetMapping("volume/details")
    public String getVolumeDetails(@RequestParam List<String> values) {
        return join(", ", values);
    }
}
