package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.BorrowModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface BorrowModelRepository extends MongoRepository<BorrowModel, UUID> {
}
