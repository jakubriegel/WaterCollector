package eu.jrie.crx.watercollector.domain.volume.calculator

import eu.jrie.crx.watercollector.domain.volume.VolumeDetails
import eu.jrie.crx.watercollector.domain.volume.offset.Offset
import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever
import spock.lang.Specification

import static eu.jrie.crx.watercollector.domain.volume.StripeCell.*

class VolumeDetailsCalculatorSpecification extends Specification {

    private final OffsetRetriever offsetRetriever = Mock()

    def "should return empty details"() {
        given:
        final surface = [1, 2, 2, 1]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.emptyDetails()

        then:
        0 * offsetRetriever._
        details == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 6,
                emptyVolume: 2,
                width: 4,
                height: 2,
                stripes: [[EMPTY, BAR], [BAR, BAR], [BAR, BAR], [EMPTY, BAR]]
        )
    }

    def "should return 0 for no-container surface"() {
        given:
        final surface = [1, 2, 2, 1]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(2, 2)
        details == volumeDetails(
               surface: surface,
               volume: 0,
               barsVolume: 6,
               emptyVolume: 2,
               width: 4,
               height: 2,
               stripes: [[EMPTY, BAR], [BAR, BAR], [BAR, BAR], [EMPTY, BAR]]
        )
    }
    
    def "should return 0 for pyramid container"() {
        given:
        final surface = [1, 2, 3, 2, 1]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(2, 2)
        details == volumeDetails(
                surface: surface,
                volume: 0,
                barsVolume: 9,
                emptyVolume: 6,
                width: 5,
                height: 3,
                stripes: [
                        [EMPTY, EMPTY, BAR], [EMPTY, BAR, BAR], [BAR, BAR, BAR], [EMPTY, BAR, BAR], [EMPTY, EMPTY, BAR]
                ]
        )
    }
    
    def "should calculate volume of simple container"() {
        given:
        final surface = [1, 0, 1]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        details == volumeDetails(
                surface: surface,
                volume: 1,
                barsVolume: 2,
                emptyVolume: 0,
                width: 3,
                height: 1,
                stripes: [[BAR], [WATER], [BAR]]
        )
    }

    def "should calculate volume of simple container with offset"() {
        given:
        final surface = [0, 0, 0, 1, 0, 1, 0, 0]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(3, 2)
        details == volumeDetails(
                surface: surface,
                volume: 1,
                barsVolume: 2,
                emptyVolume: 5,
                width: 8,
                height: 1,
                stripes: [[EMPTY], [EMPTY], [EMPTY], [BAR], [WATER], [BAR], [EMPTY], [EMPTY]]
        )
    }

    def "should calculate volume of double container with offset"() {
        given:
        final surface = [1, 0, 1, 0, 1]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        details == volumeDetails(
                surface: surface,
                volume: 2,
                barsVolume: 3,
                emptyVolume: 0,
                width: 5,
                height: 1,
                stripes: [[BAR], [WATER], [BAR], [WATER], [BAR]]
        )
    }

    def "should calculate volume of complex container with offset"() {
        given:
        final surface = [3, 0, 1, 0, 2]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        details == volumeDetails(
                surface: surface,
                volume: 5,
                barsVolume: 6,
                emptyVolume: 4,
                width: 5,
                height: 3,
                stripes: [
                        [BAR, BAR, BAR], [EMPTY, WATER, WATER], [EMPTY, WATER, BAR], [EMPTY, WATER, WATER], [EMPTY, BAR, BAR]
                ]
        )
    }

    def "should calculate volume of irregular container (CRX example 1)"() {
        given:
        final surface = [3, 2, 4, 1, 2]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        details == volumeDetails(
                surface: surface,
                volume: 2,
                barsVolume: 12,
                emptyVolume: 6,
                width: 5,
                height: 4,
                stripes: [
                        [EMPTY, BAR, BAR, BAR], [EMPTY, WATER, BAR, BAR], [BAR, BAR, BAR, BAR], [EMPTY, EMPTY, WATER, BAR], [EMPTY, EMPTY, BAR, BAR]
                ]
        )
    }

    def "should calculate volume of irregular container (CRX example 2)"() {
        given:
        final surface = [4, 1, 1, 0, 2, 3]

        when:
        final calculator = new VolumeDetailsCalculator(offsetRetriever, surface)
        final details = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        details == volumeDetails(
                surface: surface,
                volume: 8,
                barsVolume: 11,
                emptyVolume: 5,
                width: 6,
                height: 4,
                stripes: [
                        [BAR, BAR, BAR, BAR], [EMPTY, WATER, WATER, BAR], [EMPTY, WATER, WATER, BAR], [EMPTY, WATER, WATER, WATER], [EMPTY, WATER, BAR, BAR], [EMPTY, BAR, BAR, BAR]
                ]
        )
    }

    private static VolumeDetails volumeDetails(Map args) {
        return new VolumeDetails(
                args.surface, args.volume, args.barsVolume, args.emptyVolume, args.width, args.height, args.stripes
        )
    }

}
