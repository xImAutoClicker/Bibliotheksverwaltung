package de.fhdw.project.library.user.model;

import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class UserModelSearchResponse extends AbstractResponse {
    private long count;
    private List<UserModelResponse> user;
}
