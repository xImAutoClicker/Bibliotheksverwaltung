package de.fhdw.project.library.user.model;

import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserModelResponse extends AbstractResponse {
    private UUID uuid;
    private String sessionToken;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String streetNumber;
    private String zipCode;
}
