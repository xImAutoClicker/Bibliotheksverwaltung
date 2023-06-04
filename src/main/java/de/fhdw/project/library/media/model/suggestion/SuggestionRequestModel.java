package de.fhdw.project.library.media.model.suggestion;

import de.fhdw.project.library.LibraryApplication;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;

@Getter
public class SuggestionRequestModel {
    private String nameOfBook;
    private String isbn;
    private SuggestionModel.SuggestionStatusType suggestionStatusType;

    public static SuggestionRequestModel fromJson(String json){
        try{
            return LibraryApplication.getGson().fromJson(json, SuggestionRequestModel.class);
        }catch (Exception e){
            return null;
        }
    }

    public static SuggestionRequestModel fromJsonWithError(final String json) throws LibraryException {
        final SuggestionRequestModel mediaRequestModel = fromJson(json);
        if(mediaRequestModel == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return mediaRequestModel;
    }
}
