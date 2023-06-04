package de.fhdw.project.library.media.model.suggestion;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private UUID userId;

    private long createdAt;

    private SuggestionStatusType statusType = SuggestionStatusType.OPEN;

    public final SuggestionResponseModel toResponse(){
        return SuggestionResponseModel.builder()
                .uuid(this.uuid)
                .nameOfBook(this.nameOfBook)
                .isbn(this.isbn)
                .userId(this.userId)
                .createdAt(this.createdAt)
                .suggestionStatusType(this.statusType)
                .build();
    }

    public enum SuggestionStatusType {
        OPEN,
        ACCEPTED,
        DENIED;
    }
}