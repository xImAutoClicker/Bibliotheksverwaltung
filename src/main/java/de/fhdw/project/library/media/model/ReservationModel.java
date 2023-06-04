package de.fhdw.project.library.media.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_reservation")
public class ReservationModel {

    private UUID mediaId;

    private UUID userId;

    private long createdAt;

    private ReservationStatusType statusType = ReservationStatusType.OPEN;

    public enum ReservationStatusType {

        OPEN,
        ACCEPTED,
        DENIED;

    }
}
