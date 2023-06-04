package de.fhdw.project.library.media.model.borrow;

import de.fhdw.project.library.LibraryApplication;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class BorrowRequestModel {
    private UUID mediaId;
    private BorrowModel.BorrowStatusType borrowStatusType;

    public static BorrowRequestModel fromJson(String json){
        try{
            return LibraryApplication.getGson().fromJson(json, BorrowRequestModel.class);
        }catch (Exception e){
            return null;
        }
    }

    public static BorrowRequestModel fromJsonWithError(final String json) throws LibraryException {
        final BorrowRequestModel mediaRequestModel = fromJson(json);
        if(mediaRequestModel == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return mediaRequestModel;
    }
}
