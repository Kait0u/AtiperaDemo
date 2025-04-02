package pl.kaitoudev.atiperademo.rest_api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaResponseDTO;
import pl.kaitoudev.atiperademo.service.AtiperaDemoService;

/**
 * The core controller of the application - describing the REST API of the Atipera Demo project.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AtiperaDemoController {
    private AtiperaDemoService atiperaDemoService;

    /**
     * Retrieves GitHub repository information for the specified user.
     *
     * @param username GitHub username to fetch repositories for
     * @return ResponseEntity containing the user's repositories in Atipera API format
     *         with HTTP status 200 (OK) if successful
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        AtiperaResponseDTO atiperaResponseDto = atiperaDemoService.fetchGitHubRepos(username);
        return ResponseEntity.ok(atiperaResponseDto);
    }
}