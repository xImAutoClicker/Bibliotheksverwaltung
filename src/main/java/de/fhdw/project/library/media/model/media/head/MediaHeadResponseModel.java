package de.fhdw.project.library.media.model.media.head;

import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class MediaHeadResponseModel extends AbstractResponse {
    private String isbn;
    private String name;
    private String author;
    private String description;
    private LocalDate releaseDate;
}
