package de.fhdw.project.library.media.model.reservation;

import de.fhdw.project.library.LibraryApplication;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ReservationRequestModel {
    private UUID mediaId;
    private UUID userId;
    private ReservationModel.ReservationStatusType reservationStatusType;

    public static ReservationRequestModel fromJson(String json){
        try{
            return LibraryApplication.getGson().fromJson(json, ReservationRequestModel.class);
        }catch (Exception e){
            return null;
        }
    }

    public static ReservationRequestModel fromJsonWithError(final String json) throws LibraryException {
        final ReservationRequestModel mediaRequestModel = fromJson(json);
        if(mediaRequestModel == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return mediaRequestModel;
    }
}
