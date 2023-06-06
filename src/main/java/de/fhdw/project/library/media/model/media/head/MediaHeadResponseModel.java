package de.fhdw.project.library.media.model.media.head;

import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public class MediaHeadResponseModel extends AbstractResponse {
    private String isbn;
    private String name;
    private String author;
    private String description;
    private LocalDate releaseDate;
    private String cover;
    private int numberOfBooks;
}
