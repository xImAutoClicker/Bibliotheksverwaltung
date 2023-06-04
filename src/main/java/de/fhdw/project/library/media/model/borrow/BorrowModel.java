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
        return BorrowResponseModel.builder()
                .uuid(this.uuid)
                .media(mediaModelService.getMediaModelByUUIDWithOutError(this.mediaId).toResponse())
                .borrowStatusType(borrowStatusType)
                .borrowStart(this.borrowStart)
                .borrowEnd(this.borrowEnd)
                .build();
    }

    public enum BorrowStatusType {

        OPEN,
        CLOSED,
        CLOSED_RETURNED_TOO_LATE;

    }
}
