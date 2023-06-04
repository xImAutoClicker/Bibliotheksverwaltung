package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaHeadModelRepository extends MongoRepository<MediaHeadModel, String> {
    Page<MediaHeadModel> findMediaHeadModelsByNameStartingWithIgnoreCaseOrIsbnStartingWithIgnoreCaseOrAuthorStartingWithIgnoreCase(final String name, final String isbn, final String author, final Pageable pageable);
}
