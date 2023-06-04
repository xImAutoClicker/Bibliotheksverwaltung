package de.fhdw.project.library.media.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_media")
public class MediaModel {

    /**
     * Jedes Buch hat eine spezielle ID.
     *
     * Beispiel:
     * Harry Potter Teil 1 - Erstes Buch im Bestand  - diwg-fw9u-gerug-gegee
     * Harry Potter Teil 2 - Zweites Buch im Bestand - disf-fwsfu-frufsg-gfgef
     */
    @Id
    private UUID uuid;
    private String name;
    @Indexed
    private String isbn;
    private String author;
    private String description;
    private LocalDate releaseDate;
    private byte[] cover;

    public final MediaResponseModel toResponse(){
        return MediaResponseModel.builder()
                .uuid(this.uuid)
                .name(this.name)
                .isbn(this.isbn)
                .author(this.author)
                .description(this.description)
                .releaseDate(this.releaseDate)
                .build();
    }

}
