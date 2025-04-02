package pl.kaitoudev.atiperademo.rest_api.dto.github_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A DTO representing a GH repo owner.
 * It is used mainly to filter through the very detailed GitHub API reponse,
 * as only the login is required by the consumer, per specifications.
 * @see GHRepoDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GHOwnerDTO {
    @JsonProperty("login")
    private String login;
}
