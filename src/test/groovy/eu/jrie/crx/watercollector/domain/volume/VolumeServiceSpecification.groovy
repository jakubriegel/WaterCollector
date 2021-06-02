package eu.jrie.crx.watercollector.domain.volume

import eu.jrie.crx.watercollector.domain.volume.calculator.VolumeCalculator
import eu.jrie.crx.watercollector.domain.volume.calculator.VolumeCalculatorFactory
import spock.lang.Specification
import spock.lang.Unroll

class VolumeServiceSpecification extends Specification {

    private final VolumeCalculatorFactory volumeCalculatorFactory = Mock()

    private final service = new VolumeService(volumeCalculatorFactory)

    def "should return 0 for empty surface"() {
        given:
        final surface = []

        when:
        final result = service.calculateVolume(surface)

        then:
        0 * volumeCalculatorFactory._
        result == 0
    }

    def "should return 0 for single bar surface"() {
        given:
        final surface = [1]

        when:
        final result = service.calculateVolume(surface)

        then:
        0 * volumeCalculatorFactory._
        result == 0
    }

    def "should return 0 for two-bar surface"() {
        given:
        final surface = [1, 2]

        when:
        final result = service.calculateVolume(surface)

        then:
        0 * volumeCalculatorFactory._
        result == 0
    }

    def "should return 0 for same-elements surface"() {
        given:
        final surface = [3, 3, 3]

        when:
        final result = service.calculateVolume(surface)

        then:
        0 * volumeCalculatorFactory._
        result == 0
    }

    @Unroll
    def "should return calculation for complex surface - #surface"() {
        given:
        final volume = 123

        when:
        final result = service.calculateVolume(surface)

        then:
        1 * volumeCalculatorFactory.create(surface) >> Mock(VolumeCalculator) {
            1 * calculate() >> volume
        }
        result == volume

        where:
        surface << [[1, 2, 3], [1, 3, 0, 3], [4, 1, 1, 0, 2, 3]]
    }
}
