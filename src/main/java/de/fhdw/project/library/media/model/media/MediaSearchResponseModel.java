package de.fhdw.project.library.media.model.media;

import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class MediaSearchResponseModel extends AbstractResponse {
    private long count;
    private List<MediaResponseModel> media;
}
