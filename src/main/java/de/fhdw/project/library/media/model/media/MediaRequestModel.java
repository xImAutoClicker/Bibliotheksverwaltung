package de.fhdw.project.library.media.model.media;

import de.fhdw.project.library.LibraryApplication;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MediaRequestModel {
    private String name;
    private String isbn;
    private String author;
    private String description;
    private LocalDate releaseDate;
    private String cover;

    public static MediaRequestModel fromJson(String json){
        try{
            return LibraryApplication.getGson().fromJson(json, MediaRequestModel.class);
        }catch (Exception e){
            return null;
        }
    }

    public static MediaRequestModel fromJsonWithError(final String json) throws LibraryException {
        final MediaRequestModel mediaRequestModel = fromJson(json);
        if(mediaRequestModel == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return mediaRequestModel;
    }
}
