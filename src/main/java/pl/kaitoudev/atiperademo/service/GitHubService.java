package pl.kaitoudev.atiperademo.service;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kaitoudev.atiperademo.rest_api.dto.github_api.GHBranchDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.github_api.GHRepoDTO;

import java.util.List;
import java.util.Objects;

/**
 * Service that acts as a facade for interacting with GitHub's REST API.
 * Provides methods to retrieve repository and branch information for GitHub users.
 * Handles API communication, request building, and response processing.
 */
@Service
@AllArgsConstructor
public class GitHubService {
    private static final String GH_API_URL = "https://api.github.com";
    private static final HttpHeaders HEADERS = new HttpHeaders();

    static {
        // Uncomment the line below if authorization is needed. Make sure the env variable exists.
        // HEADERS.set("Authorization", "Bearer " + System.getenv("GITHUB_TOKEN"));
        HEADERS.set("Accept", "application/vnd.github.v3+json");
    }

    private final RestTemplate restTemplate;

    /**
     * Retrieves all non-fork repositories for a given GitHub user.
     * Includes branch information for each repository.
     *
     * @param username The GitHub username to fetch repositories for
     * @return List of repository DTOs with branch information, excluding forks
     * @see #getUserRepos(String, boolean)
     */
    public List<GHRepoDTO> getUserNonForkRepos(String username) {
        return getUserRepos(username, true);
    }

    /**
     * Retrieves repositories for a given GitHub user, with optional fork filtering.
     * For each repository, fetches and includes its branch information.
     *
     * @param username The GitHub username to fetch repositories for
     * @param onlyNonForks If true, returns only non-fork repositories
     * @return List of repository DTOs with branch information
     */
    public List<GHRepoDTO> getUserRepos(String username, boolean onlyNonForks) {
        String url = buildUserReposUrl(username);

        ResponseEntity<List<GHRepoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(HEADERS),
                new ParameterizedTypeReference<>() {}
        );

        List<GHRepoDTO> repoDtos = response.getBody();

        if (onlyNonForks)
            repoDtos = getNonForkRepos(repoDtos);

        Objects.requireNonNull(repoDtos).stream().parallel().forEach(repoDto -> {
            List<GHBranchDTO> branches = getBranchesForRepo(username, repoDto.getRepoName());
            repoDto.addBranches(Objects.requireNonNull(branches));
        });

        return repoDtos;
    }

    /**
     * Filters a list of repositories to include only non-fork repositories.
     *
     * @param repoDTOs List of repositories to filter
     * @return Filtered list containing only non-fork repositories
     */
    private List<GHRepoDTO> getNonForkRepos(List<GHRepoDTO> repoDTOs) {
        return Objects.requireNonNull(repoDTOs).stream()
                .filter(repoDTO -> !repoDTO.isFork())
                .toList();
    }

    /**
     * Retrieves branch information for a specific repository.
     *
     * @param username Owner of the repository
     * @param repoName Name of the repository
     * @return List of branch DTOs for the specified repository
     */
    private List<GHBranchDTO> getBranchesForRepo(String username, String repoName) {
        String url = buildRepoBranchesUrl(username, repoName);

        ResponseEntity<List<GHBranchDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(HEADERS),
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    /**
     * Builds the URL for fetching user repositories from GitHub API.
     *
     * @param username GitHub username to include in the URL
     * @return Complete URL for user repositories endpoint
     */
    private String buildUserReposUrl(String username) {
        return UriComponentsBuilder.fromUriString(GH_API_URL)
                .pathSegment("users")
                .pathSegment("{username}")
                .pathSegment("repos")
                .build(username)
                .toString();
    }

    /**
     * Builds the URL for fetching repository branches from GitHub API.
     *
     * @param owner Owner of the repository
     * @param repoName Name of the repository
     * @return Complete URL for repository branches endpoint
     */
    private String buildRepoBranchesUrl(String owner, String repoName) {
        return UriComponentsBuilder.fromUriString(GH_API_URL)
                .pathSegment("repos")
                .pathSegment("{username}")
                .pathSegment("{repoName}")
                .pathSegment("branches")
                .build(owner, repoName)
                .toString();
    }
}