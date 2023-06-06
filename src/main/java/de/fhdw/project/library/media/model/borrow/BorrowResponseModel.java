package de.fhdw.project.library.media.model.borrow;

import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class BorrowResponseModel extends AbstractResponse {
    private UUID uuid;
    private UUID userId;
    private int borrowStatusType;
    private long borrowStart;
    private long borrowEnd;
    private long returnedDate;
    private double penaltyFees;
    private MediaResponseModel media;
}
