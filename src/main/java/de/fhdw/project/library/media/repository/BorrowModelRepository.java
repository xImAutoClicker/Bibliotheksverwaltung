package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.borrow.BorrowModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface BorrowModelRepository extends MongoRepository<BorrowModel, UUID> {
    List<BorrowModel> findBorrowModelsByUserIdAndBorrowStatusType(final UUID userId, final BorrowModel.BorrowStatusType status);
    List<BorrowModel> findBorrowModelsByIsbn(final String isbn);
    List<BorrowModel> findBorrowModelsByIsbnAndBorrowStatusType(final String isbn, final BorrowModel.BorrowStatusType status);
    boolean existsBorrowModelByMediaIdAndBorrowStatusType(final UUID uuid, final BorrowModel.BorrowStatusType status);
}
