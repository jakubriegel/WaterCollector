package eu.jrie.crx.watercollector.infra.volume.offset

import spock.lang.Specification

class ReverseIteratorSpecification extends Specification {
    def "should reverse iterate"() {
        given:
        final list = [1, 2, 3, 4]

        when:
        final result = new ReverseIterator(list).collect()

        then:
        result == [4, 3, 2, 1]
    }
}
