package de.fhdw.project.library.media.model.suggestion;

import de.fhdw.project.library.media.model.suggestion.SuggestionModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class SuggestionResponseModel extends AbstractResponse {
    private UUID uuid;
    private UUID creatorId;
    private String nameOfBook;
    private String isbn;
    private String suggestionStatusType;
    private String cover;
    private long createdAt;

    public SuggestionModel.SuggestionStatusType getSuggestionStatusType() {
        return SuggestionModel.SuggestionStatusType.values()[Integer.parseInt(this.suggestionStatusType)];
    }
}
