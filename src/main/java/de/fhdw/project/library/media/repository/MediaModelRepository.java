package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.MediaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MediaModelRepository extends MongoRepository<MediaModel, UUID> {
    Page<MediaModel> findMediaModelsByNameStartingWithIgnoreCase(final String name, final Pageable pageable);
}
