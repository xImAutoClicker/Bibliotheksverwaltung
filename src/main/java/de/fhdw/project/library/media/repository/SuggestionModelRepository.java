package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.suggestion.SuggestionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface SuggestionModelRepository extends MongoRepository<SuggestionModel, UUID> {
    List<SuggestionModel> findSuggestionModelsByUserId(final UUID userId);
}
