package eu.jrie.crx.watercollector.domain.volume

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static eu.jrie.crx.watercollector.domain.volume.StripeCell.*

@SpringBootTest
class VolumeServiceDetailsIntegrationSpec extends Specification {

    @Autowired
    private VolumeService service

    def "should return empty details for empty surface"() {
        given:
        final surface = []

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 0,
                emptyVolume: 0,
                width: 0,
                height: 0,
                stripes: []
        )
    }

    def "should return empty details for flat surface"() {
        given:
        final surface = [1, 1, 1]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 3,
                emptyVolume: 0,
                width: 3,
                height: 1,
                stripes: [[BAR], [BAR], [BAR]]
        )
    }

    def "should return empty details for no-container surface"() {
        given:
        final surface = [1, 2, 3, 4, 3, 2, 1]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 16,
                emptyVolume: 12,
                width: 7,
                height: 4,
                stripes: [
                        [EMPTY, EMPTY, EMPTY, BAR], [EMPTY, EMPTY, BAR, BAR], [EMPTY, BAR, BAR, BAR],
                        [BAR, BAR, BAR, BAR], [EMPTY, BAR, BAR, BAR], [EMPTY, EMPTY, BAR, BAR],
                        [EMPTY, EMPTY, EMPTY, BAR]
                ]
        )
    }

    def "should return empty details for no-container surface with even width"() {
        given:
        final surface = [1, 2, 3, 4, 4, 3, 2, 1]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 20,
                emptyVolume: 12,
                width: 8,
                height: 4,
                stripes: [
                        [EMPTY, EMPTY, EMPTY, BAR], [EMPTY, EMPTY, BAR, BAR], [EMPTY, BAR, BAR, BAR],
                        [BAR, BAR, BAR, BAR], [BAR, BAR, BAR, BAR], [EMPTY, BAR, BAR, BAR],
                        [EMPTY, EMPTY, BAR, BAR], [EMPTY, EMPTY, EMPTY, BAR]
                ]
        )
    }

    def "should calculate empty details for simple single container surface"() {
        given:
        final surface = [3, 1, 1, 3]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 4,
                barsVolume: 8,
                emptyVolume: 0,
                width: 4,
                height: 3,
                stripes: [[BAR, BAR, BAR], [WATER, WATER, BAR], [WATER, WATER, BAR], [BAR, BAR, BAR]]
        )
    }

    def "should calculate empty details for alternated surface"() {
        given:
        final surface = [5, 1, 5, 1, 5, 1]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 8,
                barsVolume: 18,
                emptyVolume: 4,
                width: 6,
                height: 5,
                stripes: [
                        [BAR, BAR, BAR, BAR, BAR], [WATER, WATER, WATER, WATER, BAR], [BAR, BAR, BAR, BAR, BAR],
                        [WATER, WATER, WATER, WATER, BAR], [BAR, BAR, BAR, BAR, BAR], [EMPTY, EMPTY, EMPTY, EMPTY, BAR]
                ]
        )
    }

    def "should calculate empty details for complex alternated surface"() {
        given:
        final surface = [4, 1, 5, 1, 4, 1]

        when:1
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 6,
                barsVolume: 16,
                emptyVolume: 8,
                width: 6,
                height: 5,
                stripes: [
                        [EMPTY, BAR, BAR, BAR, BAR], [EMPTY, WATER, WATER, WATER, BAR], [BAR, BAR, BAR, BAR, BAR],
                        [EMPTY, WATER, WATER, WATER, BAR], [EMPTY, BAR, BAR, BAR, BAR], [EMPTY, EMPTY, EMPTY, EMPTY, BAR]
                ]
        )
    }

    def "should calculate empty details for alternated surface with borders"() {
        given:
        final surface = [5, 1, 3, 1, 5, 1]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 10,
                barsVolume: 16,
                emptyVolume: 4,
                width: 6,
                height: 5,
                stripes: [
                        [BAR, BAR, BAR, BAR, BAR], [WATER, WATER, WATER, WATER, BAR], [WATER, WATER, BAR, BAR, BAR],
                        [WATER, WATER, WATER, WATER, BAR], [BAR, BAR, BAR, BAR, BAR], [EMPTY, EMPTY, EMPTY, EMPTY, BAR]
                ]
        )
    }

    def "should calculate empty details for simple surface (CRX example 1)"() {
        given:
        final surface = [3, 2, 4, 1, 2]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 2,
                barsVolume: 12,
                emptyVolume: 6,
                width: 5,
                height: 4,
                stripes: [
                        [EMPTY, BAR, BAR, BAR], [EMPTY, WATER, BAR, BAR], [BAR, BAR, BAR, BAR],
                        [EMPTY, EMPTY, WATER, BAR], [EMPTY, EMPTY, BAR, BAR]
                ]
        )
    }

    def "should calculate empty details for simple surface (CRX example 2)"() {
        given:
        final surface = [4, 1, 1, 0, 2, 3]

        when:
        final result = service.calculateVolumeWithDetails(surface)

        then:
        result == volumeDetails(
                surface: surface,
                volume: 8,
                barsVolume: 11,
                emptyVolume: 5,
                width: 6,
                height: 4,
                stripes: [
                        [BAR, BAR, BAR, BAR], [EMPTY, WATER, WATER, BAR], [EMPTY, WATER, WATER, BAR],
                        [EMPTY, WATER, WATER, WATER], [EMPTY, WATER, BAR, BAR], [EMPTY, BAR, BAR, BAR]
                ]
        )
    }

    private static VolumeDetails volumeDetails(Map args) {
        return new VolumeDetails(
                args.surface, args.volume, args.barsVolume, args.emptyVolume, args.width, args.height, args.stripes
        )
    }

}
