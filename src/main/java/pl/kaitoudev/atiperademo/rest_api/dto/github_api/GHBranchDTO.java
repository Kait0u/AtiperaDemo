package pl.kaitoudev.atiperademo.rest_api.dto.github_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO representing a GH repository branch.
 * @see GHRepoDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GHBranchDTO implements Serializable {
    @JsonProperty("name")
    private String branchName;

    @JsonProperty("commit")
    private GHCommitDTO lastCommit;
}
