package de.fhdw.project.library.media.model.suggestion;

import de.fhdw.project.library.media.model.suggestion.SuggestionModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class SuggestionResponseModel extends AbstractResponse {
    private UUID uuid;
    private UUID userId;
    private String nameOfBook;
    private String isbn;
    private SuggestionModel.SuggestionStatusType suggestionStatusType;
    private long createdAt;
}
