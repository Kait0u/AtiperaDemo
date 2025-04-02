package pl.kaitoudev.atiperademo.rest_api.dto.atipera_api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A DTO representing a GH repository in the API response.
 * @see AtiperaResponseDTO
 */
@Getter
@Setter
public class AtiperaGHRepoDTO {
    private String name;
    private List<AtiperaGHBranchDTO> branches = new ArrayList<>();
}
