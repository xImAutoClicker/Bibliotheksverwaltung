package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.borrow.BorrowModel;
import de.fhdw.project.library.media.model.borrow.BorrowResponseModel;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.repository.BorrowModelRepository;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class BorrowModelService {

    @Autowired
    private BorrowModelRepository borrowModelRepository;

    @Autowired
    private MediaModelService mediaModelService;

    public final BorrowModel getBorrowModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<BorrowModel> borrowModel = this.borrowModelRepository.findById(uuid);
        if(borrowModel.isEmpty())
            throw new LibraryException(ErrorType.RESERVATION_NOT_FOUND);
        return borrowModel.get();
    }

    public final void saveBorrow(final BorrowModel borrowModel){
        this.borrowModelRepository.save(borrowModel);
    }

    public final void deleteReservation(final BorrowModel borrowModel) {
        this.borrowModelRepository.delete(borrowModel);
    }

    public final long countAllBorrows(){
        return this.borrowModelRepository.count();
    }

    /**
     * Ausleihen: BorrowMedia (MediaID,UserID)
     * Rückgabe: ReBorrowMedia (MediaID,UserID)
     */

    public final BorrowModel createBorrow(final UserModel userModel, final MediaModel mediaModel){
        final BorrowModel borrowModel = BorrowModel.builder()
                .uuid(this.getFreeBurrowModelId())
                .isbn(mediaModel.getIsbn())
                .mediaId(mediaModel.getUuid())
                .userId(userModel.getUuid())
                .borrowStatusType(BorrowModel.BorrowStatusType.OPEN)
                .borrowStart(Instant.now().toEpochMilli())
                .borrowEnd(Instant.now().toEpochMilli() + TimeUnit.DAYS.toMillis(3))
                .build();
        this.saveBorrow(borrowModel);
        return borrowModel;
    }

    public final BorrowModel returnMedia(final BorrowModel borrowModel){
        borrowModel.setReturnedDate(Instant.now().toEpochMilli());
        borrowModel.setBorrowStatusType(BorrowModel.BorrowStatusType.CLOSED);

        //TODO: STRAFE
        if(borrowModel.getBorrowEnd() < borrowModel.getReturnedDate()){
            borrowModel.setBorrowStatusType(BorrowModel.BorrowStatusType.CLOSED_RETURNED_TOO_LATE);
        }
        this.saveBorrow(borrowModel);
        return borrowModel;
    }

    public final List<BorrowResponseModel> getBorrows(int page, final int size){
        final List<BorrowResponseModel> toReturn = Lists.newArrayList();
        this.borrowModelRepository.findAll(PageRequest.of(--page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(mediaModelService)));
        return toReturn;
    }

    public final List<BorrowResponseModel> getBorrowsOfUser(final UserModel userModel){
        return getBorrowsOfUser(userModel, BorrowModel.BorrowStatusType.OPEN);
    }

    public final List<BorrowResponseModel> getBorrowsOfUser(final UserModel userModel, BorrowModel.BorrowStatusType status){
        final List<BorrowResponseModel> toReturn = Lists.newArrayList();
        this.borrowModelRepository.findBorrowModelsByUserIdAndBorrowStatusType(userModel.getUuid(), status).forEach(entry -> toReturn.add(entry.toResponse(mediaModelService)));
        return toReturn;
    }

    public final List<BorrowModel> getBorrowsFromIsbn(final String isbn) {
        return this.getBorrowsFromIsbn(isbn, null);
    }

    public final List<BorrowModel> getBorrowsFromIsbn(final String isbn, final BorrowModel.BorrowStatusType borrowStatusType) {
        if(borrowStatusType != null)
            return this.borrowModelRepository.findBorrowModelsByIsbnAndBorrowStatusType(isbn, borrowStatusType);
        return this.borrowModelRepository.findBorrowModelsByIsbn(isbn);
    }

    public final boolean existsBorrowFromUUID(final UUID uuid, final BorrowModel.BorrowStatusType borrowStatusType){
        return this.borrowModelRepository.existsBorrowModelByMediaIdAndBorrowStatusType(uuid, borrowStatusType);
    }

    private UUID getFreeBurrowModelId(){
        UUID uuid = UUID.randomUUID();
        while(this.borrowModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
