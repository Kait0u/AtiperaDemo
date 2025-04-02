package pl.kaitoudev.atiperademo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaErrorDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaGHBranchDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaGHRepoDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaResponseDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.github_api.GHBranchDTO;
import pl.kaitoudev.atiperademo.rest_api.dto.github_api.GHRepoDTO;

import java.util.List;

/**
 * A service providing functionalities used by this app's REST API to send back
 * a response compliant with the consumer's specifications.
 */
@Service
@AllArgsConstructor
public class AtiperaDemoService {
    private GitHubService gitHubService;

    /**
     * Fetches non-fork GitHub repositories for a user and converts them to the Atipera API format.
     *
     * @param username GitHub username to fetch repositories for
     * @return Response DTO containing user's repositories in Atipera format
     */
    public AtiperaResponseDTO fetchGitHubRepos(String username) {
        List<GHRepoDTO> userOwnRepos = gitHubService.getUserNonForkRepos(username);
        return createDemoResponse(username, userOwnRepos);
    }

    /**
     * Creates an Atipera API response from GitHub repository data.
     *
     * @param username GitHub username to include in the response
     * @param ghRepoDtos List of GitHub repository DTOs to convert
     * @return Formatted response DTO ready for API output
     */
    public AtiperaResponseDTO createDemoResponse(String username, List<GHRepoDTO> ghRepoDtos) {
        AtiperaResponseDTO responseDto = new AtiperaResponseDTO();
        responseDto.setUsername(username);
        responseDto.setRepos(
                ghRepoDtos.stream().parallel()
                        .map(this::ghRepoDtoToAtiperaGHBranchDTO).toList()
        );

        return responseDto;
    }

    /**
     * Converts a GitHub repository DTO to Atipera API format.
     *
     * @param ghRepoDto GitHub repository DTO to convert
     * @return Repository in Atipera API format
     */
    private AtiperaGHRepoDTO ghRepoDtoToAtiperaGHBranchDTO(GHRepoDTO ghRepoDto) {
        AtiperaGHRepoDTO result = new AtiperaGHRepoDTO();
        result.setName(ghRepoDto.getRepoName());
        result.setBranches(
            ghRepoDto.getBranches().stream().map(this::ghBranchDtoToAtiperaGHBranchDTO).toList()
        );
        return result;
    }

    /**
     * Converts a GitHub branch DTO to Atipera API format.
     *
     * @param ghBranchDto GitHub branch DTO to convert
     * @return Branch in Atipera API format
     */
    private AtiperaGHBranchDTO ghBranchDtoToAtiperaGHBranchDTO(GHBranchDTO ghBranchDto) {
        AtiperaGHBranchDTO result = new AtiperaGHBranchDTO();
        result.setName(ghBranchDto.getBranchName());
        result.setSha(ghBranchDto.getLastCommit().getSha());
        return result;
    }

    /**
     * Creates an error response in Atipera API format.
     *
     * @param statusCode HTTP status code for the error
     * @param message Descriptive error message
     * @return Error DTO ready for API output
     */
    public AtiperaErrorDTO createErrorResponse(int statusCode, String message) {
        AtiperaErrorDTO responseDto = new AtiperaErrorDTO();
        responseDto.setStatus(statusCode);
        responseDto.setMessage(message);
        return responseDto;
    }
}
