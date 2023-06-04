package de.fhdw.project.library.media.model.reservation;

import de.fhdw.project.library.media.service.MediaModelService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_reservation")
public class ReservationModel {

    @Id
    private UUID uuid;

    private UUID mediaId;

    private UUID userId;

    private long createdAt;

    private ReservationStatusType statusType = ReservationStatusType.OPEN;


    public final ReservationResponseModel toResponse(final MediaModelService mediaModelService) {
        return ReservationResponseModel.builder()
                .uuid(this.uuid)
                .createdAt(this.createdAt)
                .media(mediaModelService.getMediaModelByUUIDWithOutError(this.mediaId).toResponse())
                .userId(this.userId)
                .reservationStatusType(this.statusType)
                .build();
    }

    public enum ReservationStatusType {

        OPEN,
        ACCEPTED,
        DENIED;
    }
}
