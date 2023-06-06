package de.fhdw.project.library.media.model.media.head;

import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.media.service.MediaModelService;
import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_media_head")
public class MediaHeadModel {

    @Id
    private String isbn;
    private String name;
    private String author;
    private String description;
    private LocalDate releaseDate;
    private byte[] cover;

    public final MediaHeadResponseModel toResponse() {
        return toResponse(-1);
    }

    public final MediaHeadResponseModel toResponse(int numberOfBooks) {
        return MediaHeadResponseModel
                .builder()
                .isbn(this.isbn)
                .name(this.name)
                .author(this.author)
                .description(this.description)
                .releaseDate(this.releaseDate)
                .cover(Base64.encodeBase64String(this.cover))
                .numberOfBooks(numberOfBooks)
                .build();
    }
}
