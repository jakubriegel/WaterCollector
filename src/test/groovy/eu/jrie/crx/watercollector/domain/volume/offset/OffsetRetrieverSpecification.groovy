package eu.jrie.crx.watercollector.domain.volume.offset

import spock.lang.Specification

class OffsetRetrieverSpecification extends Specification {

    private static final retriever = new OffsetRetriever()

    def "should retrieve offset for left-stair surface"() {
        given:
        final surface = [0, 1, 2, 3]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(3, 0)
    }

    def "should retrieve offset for right-stair surface"() {
        given:
        final surface = [3, 2, 1, 0]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(0, 3)
    }

    def "should retrieve offset for pyramid surface"() {
        given:
        final surface = [0, 1, 2, 1, 0]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(2, 2)
    }

    def "should retrieve offset for irregular surface"() {
        given:
        final surface = [0, 2, 3, 2, 4, 1, 2, 2, 1]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(2, 2)
    }

    def "should retrieve offset for irregular surface (CRX example 1)"() {
        given:
        final surface = [3, 2, 4, 1, 2]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(0, 0)
    }

    def "should retrieve offset for irregular surface (CRX example 2)"() {
        given:
        final surface = [4, 1, 1, 0, 2, 3]

        when:
        final offset = retriever.retrieveOffset(surface)

        then:
        offset == new Offset(0, 0)
    }
}
