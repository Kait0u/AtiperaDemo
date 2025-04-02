package pl.kaitoudev.atiperademo.rest_api.dto.atipera_api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A DTO that represents a response of the API to be sent back to the consumer.
 */
@Getter
@Setter
public class AtiperaResponseDTO {
    private String username;
    private List<AtiperaGHRepoDTO> repos = new ArrayList<>();
}
