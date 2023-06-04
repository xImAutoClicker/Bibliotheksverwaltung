package de.fhdw.project.library.media.model.media.head;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
        return MediaHeadResponseModel
                .builder()
                .isbn(this.isbn)
                .name(this.name)
                .author(this.author)
                .description(this.description)
                .releaseDate(this.releaseDate)
                .build();
    }
}
