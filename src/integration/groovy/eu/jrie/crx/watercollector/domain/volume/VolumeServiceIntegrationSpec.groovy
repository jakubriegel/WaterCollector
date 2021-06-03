package eu.jrie.crx.watercollector.domain.volume

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class VolumeServiceIntegrationSpec extends Specification {

    @Autowired
    private VolumeService service

    def "should return 0 for empty surface"() {
        given:
        final surface = []

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 0
    }

    def "should return 0 for flat surface"() {
        given:
        final surface = [1, 1, 1]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 0
    }

    def "should return 0 for no-container surface"() {
        given:
        final surface = [1, 2, 3, 4, 3, 2, 1]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 0
    }

    def "should return 0 for no-container surface with even width"() {
        given:
        final surface = [1, 2, 3, 4, 4, 3, 2, 1]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 0
    }

    def "should calculate volume for simple single container surface"() {
        given:
        final surface = [3, 1, 1, 3]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 4
    }

    def "should calculate volume for alternated surface"() {
        given:
        final surface = [5, 1, 5, 1, 5, 1]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 8
    }

    def "should calculate volume for complex alternated surface"() {
        given:
        final surface = [4, 1, 5, 1, 4, 1]

        when:1
        final result = service.calculateVolume(surface)

        then:
        result == 6
    }

    def "should calculate volume for alternated surface with borders"() {
        given:
        final surface = [5, 1, 3, 1, 5, 1]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 10
    }

    def "should calculate volume for simple surface (CRX example 1)"() {
        given:
        final surface = [3, 2, 4, 1, 2]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 2
    }

    def "should calculate volume for simple surface (CRX example 2)"() {
        given:
        final surface = [4, 1, 1, 0, 2, 3]

        when:
        final result = service.calculateVolume(surface)

        then:
        result == 8
    }
}
