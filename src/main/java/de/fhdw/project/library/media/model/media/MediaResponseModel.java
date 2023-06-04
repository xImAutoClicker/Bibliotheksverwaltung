package de.fhdw.project.library.media.model.media;

import de.fhdw.project.library.media.model.media.head.MediaHeadResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class MediaResponseModel extends AbstractResponse {
    private UUID uuid;
    private MediaHeadResponseModel mediaHead;
}
