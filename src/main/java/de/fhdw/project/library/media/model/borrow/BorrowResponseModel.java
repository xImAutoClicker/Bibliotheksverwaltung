package de.fhdw.project.library.media.model.borrow;

import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class BorrowResponseModel extends AbstractResponse {
    private UUID uuid;
    private MediaResponseModel media;
    private UUID userId;
    private String isbn;
    private BorrowModel.BorrowStatusType borrowStatusType;
    private Long borrowStart;
    private Long borrowEnd;
    private Long returnedDate;
}
