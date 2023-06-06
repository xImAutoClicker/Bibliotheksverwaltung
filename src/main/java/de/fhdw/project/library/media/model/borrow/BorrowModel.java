package de.fhdw.project.library.media.model.borrow;

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
@Document("col_borrow")
public class BorrowModel {

    /**
     * ID of BorrowModel
     */
    @Id
    private UUID uuid;

    /**
     * ID of borrowed Media
     */
    @Indexed
    private UUID mediaId;

    @Indexed
    private String isbn;

    /**
     * ID of user who have borrowed the Media
     */
    @Indexed
    private UUID userId;

    private BorrowStatusType borrowStatusType;

    private long borrowStart;

    private long borrowEnd;

    private long returnedDate;

    public final BorrowResponseModel toResponse(MediaModelService mediaModelService) {
        double penaltyFees = this.getOverdueDays() * 5; // 5€ per Day

        return BorrowResponseModel.builder()
                .uuid(this.uuid)
                .media(mediaModelService.getMediaModelByUUIDWithOutError(this.mediaId).toResponse(mediaModelService.getMediaHeadModelService()))
                .userId(this.userId)
                .borrowStatusType(borrowStatusType.ordinal())
                .borrowStart(this.borrowStart)
                .borrowEnd(this.borrowEnd)
                .returnedDate(this.returnedDate)
                .penaltyFees(penaltyFees)
                .build();
    }

    public long getOverdueDays() {
        long currentDate = System.currentTimeMillis();

        if (currentDate > this.borrowEnd && this.returnedDate == 0) {
            long overdueMillis = currentDate - this.borrowEnd;
            long overdueDays = overdueMillis / (24 * 60 * 60 * 1000);
            return overdueDays;
        } else if (this.returnedDate > this.borrowEnd) {
            long overdueMillis = this.returnedDate - this.borrowEnd;
            long overdueDays = overdueMillis / (24 * 60 * 60 * 1000);
            return overdueDays;
        } else {
            return 0;
        }
    }

    public enum BorrowStatusType {

        OPEN,
        CLOSED,
        CLOSED_RETURNED_TOO_LATE;

    }
}
