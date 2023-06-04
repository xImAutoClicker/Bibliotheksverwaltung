package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.SuggestionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SuggestionModelRepository extends MongoRepository<SuggestionModel, UUID> {
}
