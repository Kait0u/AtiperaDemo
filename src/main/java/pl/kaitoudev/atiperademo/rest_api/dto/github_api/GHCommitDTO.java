package pl.kaitoudev.atiperademo.rest_api.dto.github_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A DTO representing GH commit information for a certan repo.
 * @see GHBranchDTO
 * @see GHRepoDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GHCommitDTO {
    @JsonProperty("sha")
    private String sha;
}
