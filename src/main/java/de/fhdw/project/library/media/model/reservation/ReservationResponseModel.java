package de.fhdw.project.library.media.model.reservation;

import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.media.model.reservation.ReservationModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class ReservationResponseModel extends AbstractResponse {
    private UUID uuid;
    private MediaResponseModel media;
    private UUID userId;
    private Long createdAt;
    private String reservationStatusType;
    private Long estimatedDeliveryOn;

    public ReservationModel.ReservationStatusType getReservationStatusType() {
        return ReservationModel.ReservationStatusType.values()[Integer.parseInt(this.reservationStatusType)];
    }
}
