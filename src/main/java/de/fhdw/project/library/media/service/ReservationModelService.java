package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.reservation.ReservationModel;
import de.fhdw.project.library.media.model.reservation.ReservationResponseModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionResponseModel;
import de.fhdw.project.library.media.repository.ReservationModelRepository;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationModelService {
    @Autowired
    private ReservationModelRepository reservationModelRepository;
    @Autowired
    private MediaModelService mediaModelService;

    @Autowired
    private BorrowModelService borrowModelService;

    public final ReservationModel getReservationModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<ReservationModel> reservationModel = this.reservationModelRepository.findById(uuid);
        if(reservationModel.isEmpty())
            throw new LibraryException(ErrorType.RESERVATION_NOT_FOUND);
        return reservationModel.get();
    }

    public final void saveReservation(final ReservationModel reservationModel){
        this.reservationModelRepository.save(reservationModel);
    }

    public final void deleteReservation(final ReservationModel reservationModel) {
        this.reservationModelRepository.delete(reservationModel);
    }

    public final long countAllReservations(){
        return this.reservationModelRepository.count();
    }

    public final ReservationModel createReservation(final MediaModel mediaModel, final UserModel userModel, final ReservationModel.ReservationStatusType reservationStatusType) {
        final ReservationModel reservationModel = ReservationModel.builder()
                .uuid(getFreeReservationUUID())
                .mediaId(mediaModel.getUuid())
                .userId(userModel.getUuid())
                .createdAt(Instant.now().toEpochMilli())
                .statusType(reservationStatusType).build();

        this.saveReservation(reservationModel);
        return reservationModel;
    }

    public final ReservationModel updateStatus(final ReservationModel reservationModel, final ReservationModel.ReservationStatusType reservationStatusType){
        reservationModel.setStatusType(reservationStatusType);
        this.saveReservation(reservationModel);
        return reservationModel;
    }

    public final List<ReservationResponseModel> getReservations(int page, final int size){
        final List<ReservationResponseModel> toReturn = Lists.newArrayList();
        this.reservationModelRepository.findAll(PageRequest.of(--page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(this.mediaModelService, this.borrowModelService)));
        return toReturn;
    }

    public final List<ReservationResponseModel> getReservationsOfUser(final UserModel userModel){
        final List<ReservationResponseModel> toReturn = Lists.newArrayList();
        this.reservationModelRepository.findReservationModelsByUserId(userModel.getUuid()).forEach(entry -> toReturn.add(entry.toResponse(this.mediaModelService, this.borrowModelService)));
        return toReturn;
    }

    private UUID getFreeReservationUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.reservationModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }

    public List<ReservationResponseModel> searchReservation(int page, int size) {
        page--;
        final List<ReservationResponseModel> toReturn = Lists.newArrayList();

        this.reservationModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(this.mediaModelService, this.borrowModelService)));

        return toReturn;
    }
}
