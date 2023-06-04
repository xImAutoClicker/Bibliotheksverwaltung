package de.fhdw.project.library.media.model.reservation;

import de.fhdw.project.library.media.model.reservation.ReservationResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class ReservationSearchResponseModel extends AbstractResponse {
    private long count;
    private List<ReservationResponseModel> reservations;
}
