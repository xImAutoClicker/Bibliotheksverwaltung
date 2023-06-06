package de.fhdw.project.library.media.model.suggestion;

import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_suggestion")
public class SuggestionModel {

    @Id
    private UUID uuid;

    private String nameOfBook;

    private String isbn;

    private byte[] cover;

    private UUID userId;

    private long createdAt;

    private SuggestionStatusType statusType = SuggestionStatusType.OPEN;

    public final SuggestionResponseModel toResponse(){
        return SuggestionResponseModel.builder()
                .uuid(this.uuid)
                .nameOfBook(this.nameOfBook)
                .isbn(this.isbn)
                .creatorId(this.userId)
                .createdAt(this.createdAt)
                .suggestionStatusType(String.valueOf(this.statusType.ordinal()))
                .cover(Base64.encodeBase64String(cover))
                .build();
    }

    public enum SuggestionStatusType {
        OPEN,
        ACCEPTED,
        DENIED;
    }
}
