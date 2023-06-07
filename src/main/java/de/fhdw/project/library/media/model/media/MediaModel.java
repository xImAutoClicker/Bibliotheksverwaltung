package de.fhdw.project.library.media.model.media;

import de.fhdw.project.library.media.model.borrow.BorrowModel;
import de.fhdw.project.library.media.service.BorrowModelService;
import de.fhdw.project.library.media.service.MediaHeadModelService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
     * Harry Potter Teil 1 - Erstes Buch im Bestand  - asd-asd-asd-asd
     * Harry Potter Teil 2 - Zweites Buch im Bestand - disf-fwsfu-frufsg-gfgef
     */
    @Id
    private UUID uuid;

    @Indexed
    private String isbn;

    public final MediaResponseModel toResponse(final MediaHeadModelService mediaHeadModelService){
        return MediaResponseModel.builder()
                .mediaId(this.uuid)
                .mediaHead(mediaHeadModelService.getMediaHeadModelByISBNWithOutError(this.isbn).toResponse())
                .build();
    }

}
