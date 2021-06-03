package eu.jrie.crx.watercollector

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ApplicationIntegrationSpec extends Specification {
    def "should start the app"() {
        expect:
        true
    }
}
