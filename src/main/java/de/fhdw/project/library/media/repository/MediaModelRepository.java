package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.media.MediaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MediaModelRepository extends MongoRepository<MediaModel, UUID> {
    Page<MediaModel> findMediaModelsByIsbnStartingWithIgnoreCase(final String isbn, final Pageable pageable);
}
