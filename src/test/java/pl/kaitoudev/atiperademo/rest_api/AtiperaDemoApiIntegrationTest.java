package pl.kaitoudev.atiperademo.rest_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.*;

import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaErrorDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaGHBranchDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaGHRepoDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaResponseDTO;

import java.util.List;
import java.util.Objects;

/**
 * A class containing integration tests for the Atipera Demo API recruitment application.
 * Be aware: I would like to learn how to properly create integration tests - I would need some guidance.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AtiperaDemoApiIntegrationTest {
    private static final String EXISTENT_USERNAME = "Kait0u";
    private static final String SUCCESS_MOCK_RESPONSE = """
    {"username":"Kait0u","repos":[{"name":"CellularAutomata","branches":[{"name":"master","sha":"770e81da62c022c5ddabac154e4d3847c58ad96d"}]},{"name":"Clone2048","branches":[{"name":"master","sha":"6f6b8a8d2b4394febfad1e4f66bfef9fae61fea9"}]},{"name":"CodeGolf","branches":[{"name":"master","sha":"ecb881645a0047c4d26f5b2e664f351b786c81d8"}]},{"name":"Kait0u","branches":[{"name":"main","sha":"54cb4fa5ebfdc32c3d6867f3297032a724739484"}]},{"name":"LeetCode","branches":[{"name":"master","sha":"d4e4065a675bddc1461a9ea14bdf58b8e5adb874"}]},{"name":"MultiDraw","branches":[{"name":"master","sha":"d594c7ddd7953315b85003497dd4aa096773f2f3"}]},{"name":"notepad","branches":[{"name":"master","sha":"48818d73e261ee56ad862852245222322b375e00"}]},{"name":"NumlockToggler","branches":[{"name":"main","sha":"24c9982e463b4b021c52d28d57e56567af0d864b"}]},{"name":"PyMage","branches":[{"name":"master","sha":"ceccd4d5c5bba67841819a3bcff1c5d738eaf855"}]},{"name":"RandomImage","branches":[{"name":"main","sha":"7ea8c99670566cf47f429fd63b397cc8790eed22"}]},{"name":"RecoDigit","branches":[{"name":"master","sha":"48537d64f95236f2cad9b567624cd593735a683e"}]},{"name":"SpaceInvadersClone","branches":[{"name":"master","sha":"c2f5db6a0a1e56300f94a341d996fd9def8dc482"}]},{"name":"spring-todo","branches":[{"name":"master","sha":"d182c542d99b0c388386a3389d5d2af735792262"}]},{"name":"StuData","branches":[{"name":"master","sha":"040ad0571c3339fceb927a0634d0eb83aea77f4c"}]},{"name":"sudoku-generator","branches":[{"name":"master","sha":"8bad8c41595fb3b2760ab92598fbc9c92420313c"}]},{"name":"sudoku-html","branches":[{"name":"master","sha":"efda9559190c61a1b5487fd385ea536a6e2d815c"}]},{"name":"Tranquilizer","branches":[{"name":"master","sha":"3025526ec15ebc949aec6ba6c8fd2b94175f2483"}]},{"name":"urlittle","branches":[{"name":"master","sha":"da7748331da56d266e19ed60914eef5956e1ba47"}]}]}
    """;

    private static final String NON_EXISTENT_USERNAME = "efsbbiwgwbu";
    private static final String FAILURE_MOCK_RESPONSE = """
    {"status": "404","message": "Not found"}
    """;

    private MockRestServiceServer mockServer;
    @Autowired private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getAtiperaDemoResponse_shouldReturnAtiperaResponseDto_whenApiReturns200() {
        // Arrange
        String username = EXISTENT_USERNAME;

        // 200 case
        mockServer.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(String.format("/api/%s", EXISTENT_USERNAME))
        ).andExpect(
                MockRestRequestMatchers.method(HttpMethod.GET)
        ).andRespond(
                MockRestResponseCreators.withSuccess(SUCCESS_MOCK_RESPONSE, MediaType.APPLICATION_JSON)
        );

        // Act
        ResponseEntity<AtiperaResponseDTO> exchange = restTemplate.exchange(
                String.format("/api/%s", username),
                HttpMethod.GET,
                null,
                AtiperaResponseDTO.class
        );
        AtiperaResponseDTO responseDTO = exchange.getBody();

        List<String> shas = Objects.requireNonNull(responseDTO).getRepos().stream()
                .map(AtiperaGHRepoDTO::getBranches)
                .map(branches -> branches.stream().map(AtiperaGHBranchDTO::getSha).toList())
                .flatMap(List::stream).toList();

        // Assert
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getRepos()).hasSize(18);
        assertThat(responseDTO.getRepos()).extracting("name").contains("CellularAutomata", "Clone2048", "urlittle", "Tranquilizer");
        assertThat(shas).doesNotHaveDuplicates();
    }

    @Test
    public void getAtiperaDemoResponseUsingNonexistentUsername_shouldReturnAtiperaErrorDto_whenApiReturns404() {
        // Arrange
        String username = NON_EXISTENT_USERNAME;

        // 404 case
        mockServer.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(String.format("/api/%s", username))
        ).andExpect(
                MockRestRequestMatchers.method(HttpMethod.GET)
        ).andRespond(
                MockRestResponseCreators.withSuccess().body(FAILURE_MOCK_RESPONSE).contentType(MediaType.APPLICATION_JSON)
        );

        // Act
        AtiperaErrorDTO responseDTO = restTemplate.exchange(
                String.format("/api/%s", username),
                HttpMethod.GET,
                null,
                AtiperaErrorDTO.class
        ).getBody();

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getStatus()).isEqualTo(404);
        assertThat(responseDTO.getMessage()).isEqualTo("Not found");
    }
}
