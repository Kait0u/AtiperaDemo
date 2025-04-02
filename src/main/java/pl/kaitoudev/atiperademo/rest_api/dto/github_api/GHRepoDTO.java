package pl.kaitoudev.atiperademo.rest_api.dto.github_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A DTO representing a GH repository.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GHRepoDTO {
    @JsonProperty("name")
    private String repoName;

    @JsonProperty("owner")
    private GHOwnerDTO owner;

    @JsonProperty("fork")
    private boolean fork;

    private final List<GHBranchDTO> branches = new ArrayList<>();

    /**
     * Adds a single branch to this repository.
     *
     * @param branch The branch to add to this repository
     */
    @SuppressWarnings("unused")
    public void addBranch(GHBranchDTO branch) {
        this.branches.add(branch);
    }

    /**
     * Adds multiple branches to this repository.
     *
     * @param branches Collection of branches to add to this repository
     */
    public void addBranches(Collection<GHBranchDTO> branches) {
        this.branches.addAll(branches);
    }
}
