package de.fhdw.project.library.media.model;

import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class MediaResponseModel extends AbstractResponse {
    private UUID uuid;
    private String name;
    private String isbn;
    private String author;
    private String description;
    private LocalDate releaseDate;
}
