package pl.kaitoudev.atiperademo.rest_api.dto.atipera_api;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO that will be JSON-mapped into respective branches for a certain Repository
 * @see AtiperaGHRepoDTO
 * @see AtiperaResponseDTO
 */
@Getter
@Setter
public class AtiperaGHBranchDTO {
    private String name;
    private String sha;
}
