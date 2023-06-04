package de.fhdw.project.library.media.model;

import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class MediaSearchResponseModel extends AbstractResponse {
    private long count;
    private List<MediaResponseModel> media;
}
