package de.fhdw.project.library.media.model;

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
     * ID of BorrowEntry
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

    private long borrowStart;

    private long borrowEnd;
}
