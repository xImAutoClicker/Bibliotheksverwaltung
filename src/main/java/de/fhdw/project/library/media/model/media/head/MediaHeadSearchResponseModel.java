package de.fhdw.project.library.media.model.media.head;

import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class MediaHeadSearchResponseModel extends AbstractResponse {
    private long count;
    private List<MediaHeadResponseModel> mediaHead;
}
