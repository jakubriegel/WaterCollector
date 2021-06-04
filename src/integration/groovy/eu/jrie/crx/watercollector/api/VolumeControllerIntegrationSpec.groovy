package eu.jrie.crx.watercollector.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
class VolumeControllerIntegrationSpec extends Specification {

    private static final VOLUME_PATH = "/water/volume"
    private static final DETAILS_PATH = "$VOLUME_PATH/details"

    @Autowired
    private MockMvc mvc

    def "should return volume for given surface"() {
        given:
        final request = get(VOLUME_PATH)
                .param("bars", "1,3,0,1,2")
                .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json("""
                {
                  "surface": [1, 3, 0, 1, 2],
                  "volume": 3
                }
            """))
    }

    def "should return 0 for empty surface"() {
        given:
        final request = get(VOLUME_PATH)
            .param("bars", "")
            .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json("""
                {
                  "surface": [],
                  "volume": 0
                }
            """))
    }

    def "should return status 400 when parameter is missing"() {
        given:
        final request = get(VOLUME_PATH)
            .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json("""
                {
                  "code": "MISSING_PARAMETER",
                  "path": "/water/volume",
                  "message": "The request is missing required parameters.",
                  "details": {
                    "requiredParameters": ["bars"]
                  }
                }
            """))
    }

    def "should return status 400 when surface contains negative bars"() {
        given:
        final request = get(VOLUME_PATH)
            .param("bars", "-1")
            .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json("""
                {
                  "code": "INVALID_BAR_HEIGHT",
                  "path": "/water/volume",
                  "message": "Provided surface contains negative bar heights.",
                  "details": {
                    "surface": [-1]
                  }
                }
            """))
    }

    def "should return volume for given surface with details"() {
        given:
        final request = get(DETAILS_PATH)
                .param("bars", "1,3,0,1,2")
                .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                {
                      "surface": [1, 3, 0, 1, 2],
                      "volume": 3,
                      "barsVolume": 7,
                      "emptyVolume": 5,
                      "width": 5,
                      "height": 3,
                      "stripes": [
                        ["EMPTY", "EMPTY", "BAR"], ["BAR", "BAR", "BAR"], ["EMPTY", "WATER", "WATER"],
                        ["EMPTY", "WATER", "BAR"], ["EMPTY", "BAR", "BAR"]
                      ]
                    }
            """))
    }

    def "should return 0 for empty surface with details"() {
        given:
        final request = get(DETAILS_PATH)
                .param("bars", "")
                .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                {
                  "surface": [],
                  "volume": 0,
                  "barsVolume": 0,
                  "emptyVolume": 0,
                  "width": 0,
                  "height": 0,
                  "stripes": []
                }
            """))
    }

    def "should return status 400 when parameter is missing with details"() {
        given:
        final request = get(DETAILS_PATH)
                .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                {
                  "code": "MISSING_PARAMETER",
                  "path": "/water/volume/details",
                  "message": "The request is missing required parameters.",
                  "details": {
                    "requiredParameters": ["bars"]
                  }
                }
            """))
    }

    def "should return status 400 when surface contains negative bars with details"() {
        given:
        final request = get(DETAILS_PATH)
                .param("bars", "-1")
                .accept(APPLICATION_JSON)

        expect:
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                {
                  "code": "INVALID_BAR_HEIGHT",
                  "path": "/water/volume/details",
                  "message": "Provided surface contains negative bar heights.",
                  "details": {
                    "surface": [-1]
                  }
                }
            """))
    }
}
