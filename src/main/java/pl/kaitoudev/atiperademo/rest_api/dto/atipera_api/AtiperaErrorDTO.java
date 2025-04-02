package pl.kaitoudev.atiperademo.rest_api.dto.atipera_api;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO that represents an error response of the API.
 */
@Getter
@Setter
public class AtiperaErrorDTO {
    private int status;
    private String message;
}
