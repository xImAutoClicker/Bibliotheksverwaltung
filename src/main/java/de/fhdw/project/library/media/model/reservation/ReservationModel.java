package de.fhdw.project.library.media.model.reservation;

import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.service.BorrowModelService;
import de.fhdw.project.library.media.service.MediaHeadModelService;
import de.fhdw.project.library.media.service.MediaModelService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed
    private UUID mediaId;

    @Indexed
    private UUID userId;

    private long createdAt;

    @Builder.Default
    private ReservationStatusType statusType = ReservationStatusType.OPEN;

    public final ReservationResponseModel toResponse(final MediaModelService mediaModelService, final BorrowModelService borrowModelService) {
        MediaModel mediaModel = mediaModelService.getMediaModelByUUIDWithOutError(this.mediaId);

        return ReservationResponseModel.builder()
                .uuid(this.uuid)
                .createdAt(this.createdAt)
                .media(mediaModel.toResponse(mediaModelService.getMediaHeadModelService()))
                .userId(this.userId)
                .reservationStatusType(String.valueOf(this.statusType.ordinal()))
                .estimatedDeliveryOn(mediaModelService.getEstimatedDelivery(mediaModel.getIsbn(), borrowModelService))
                .build();
    }

    public enum ReservationStatusType {

        OPEN,
        ACCEPTED,
        DENIED;
    }
}
