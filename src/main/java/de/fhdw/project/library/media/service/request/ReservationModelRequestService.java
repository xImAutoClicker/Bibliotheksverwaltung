package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.reservation.ReservationModel;
import de.fhdw.project.library.media.model.reservation.ReservationRequestModel;
import de.fhdw.project.library.media.model.reservation.ReservationSearchResponseModel;
import de.fhdw.project.library.media.service.MediaModelService;
import de.fhdw.project.library.media.service.ReservationModelService;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.service.UserModelService;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationModelRequestService {
    @Autowired
    private ReservationModelService reservationModelService;
    @Autowired
    private MediaModelService mediaModelService;
    @Autowired
    private UserModelService userModelService;

    public final ResponseEntity<String> createReservation(final String auth, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        final ReservationRequestModel reservationRequestModel = ReservationRequestModel.fromJsonWithError(body);
        if(reservationRequestModel.getMediaId() == null || reservationRequestModel.getUserId() == null || reservationRequestModel.getReservationStatusType() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        final MediaModel mediaModel = this.mediaModelService.getMediaModelByUUID(reservationRequestModel.getMediaId());
        return this.reservationModelService.createReservation(mediaModel, userModel, reservationRequestModel.getReservationStatusType()).toResponse(mediaModelService).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> deleteReservation(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final ReservationModel reservationModel = this.reservationModelService.getReservationModelByUUID(uuid);

        if(!requestedModel.isTeam() && !reservationModel.getUserId().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        this.reservationModelService.deleteReservation(reservationModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public final ResponseEntity<String> updateReservation(final String auth, final UUID uuid, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        if(!userModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final ReservationModel reservationModel = this.reservationModelService.getReservationModelByUUID(uuid);
        final ReservationRequestModel reservationRequestModel = ReservationRequestModel.fromJsonWithError(body);

        this.reservationModelService.updateStatus(reservationModel, reservationRequestModel.getReservationStatusType());
        this.reservationModelService.saveReservation(reservationModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public final ResponseEntity<String> getReservations(final String auth, final int page, final int size) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return ReservationSearchResponseModel.builder().reservations(this.reservationModelService.getReservations(page, size)).count(this.reservationModelService.countAllReservations()).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getReservation(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final ReservationModel reservationModel = this.reservationModelService.getReservationModelByUUID(uuid);

        if(!requestedModel.isTeam() && !reservationModel.getUserId().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        return reservationModel.toResponse(mediaModelService).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getReservationsOfUser(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetModel = this.userModelService.getUserModelByUUID(uuid);

        if(!requestedModel.isTeam() && !requestedModel.getUuid().equals(targetModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        return ReservationSearchResponseModel.builder().reservations(this.reservationModelService.getReservationsOfUser(targetModel)).build().toResponseEntity(HttpStatus.OK);
    }
}
