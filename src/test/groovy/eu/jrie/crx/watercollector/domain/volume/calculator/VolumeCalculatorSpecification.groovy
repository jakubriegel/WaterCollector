package eu.jrie.crx.watercollector.domain.volume.calculator

import eu.jrie.crx.watercollector.domain.volume.offset.Offset
import eu.jrie.crx.watercollector.domain.volume.offset.OffsetRetriever
import spock.lang.Specification

class VolumeCalculatorSpecification extends Specification {

    private final OffsetRetriever offsetRetriever = Mock()

    def "should return 0 for no-container surface"() {
        given:
        final surface = [1, 2, 2, 1]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(2, 2)
        volume == 0
    }
    
    def "should return 0 for pyramid container"() {
        given:
        final surface = [1, 2, 3, 2, 1]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(2, 2)
        volume == 0
    }
    
    def "should calculate volume of simple container"() {
        given:
        final surface = [1, 0, 1]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        volume == 1
    }

    def "should calculate volume of simple container with offset"() {
        given:
        final surface = [-2, -2, -2, 1, 0, 1, -2, -2]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(3, 2)
        volume == 1
    }

    def "should calculate volume of double container with offset"() {
        given:
        final surface = [1, 0, 1, 0, 1]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        volume == 2
    }

    def "should calculate volume of complex container with offset"() {
        given:
        final surface = [3, 0, 1, 0, 2]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        volume == 5
    }

    def "should calculate volume of irregular container (CRX example 1)"() {
        given:
        final surface = [3, 2, 4, 1, 2]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        volume == 2
    }

    def "should calculate volume of irregular container (CRX example 2)"() {
        given:
        final surface = [4, 1, 1, 0, 2, 3]

        when:
        final calculator = new VolumeCalculator(offsetRetriever, surface)
        final volume = calculator.calculate()

        then:
        1 * offsetRetriever.retrieveOffset(surface) >> new Offset(0, 0)
        volume == 8
    }
}
