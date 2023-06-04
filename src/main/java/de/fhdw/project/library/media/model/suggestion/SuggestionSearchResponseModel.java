package de.fhdw.project.library.media.model.suggestion;

import de.fhdw.project.library.media.model.suggestion.SuggestionResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class SuggestionSearchResponseModel extends AbstractResponse {
    private long count;
    private List<SuggestionResponseModel> suggestions;
}
