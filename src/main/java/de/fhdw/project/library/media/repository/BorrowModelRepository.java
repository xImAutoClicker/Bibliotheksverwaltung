package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.borrow.BorrowModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface BorrowModelRepository extends MongoRepository<BorrowModel, UUID> {
    List<BorrowModel> findBorrowModelsByUserId(final UUID userId);
}
