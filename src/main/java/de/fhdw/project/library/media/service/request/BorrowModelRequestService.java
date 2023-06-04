package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.borrow.BorrowModel;
import de.fhdw.project.library.media.model.borrow.BorrowRequestModel;
import de.fhdw.project.library.media.model.borrow.BorrowSearchResponseModel;
import de.fhdw.project.library.media.service.BorrowModelService;
import de.fhdw.project.library.media.service.MediaModelService;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.service.UserModelService;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BorrowModelRequestService {

    @Autowired
    private BorrowModelService borrowModelService;

    @Autowired
    private UserModelService userModelService;

    @Autowired
    private MediaModelService mediaModelService;

    public final ResponseEntity<String> createBorrow(final String auth, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        final BorrowRequestModel borrowRequestModel = BorrowRequestModel.fromJsonWithError(body);

        if(borrowRequestModel.getMediaId() == null || borrowRequestModel.getBorrowStatusType() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return this.borrowModelService.createBorrow(userModel, this.mediaModelService.getMediaModelByUUID(borrowRequestModel.getMediaId())).toResponse(this.mediaModelService).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> returnMedia(final String auth, final UUID uuid) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);

        final BorrowModel borrowModel = this.borrowModelService.getBorrowModelByUUID(uuid);

        this.borrowModelService.returnMedia(borrowModel);
        this.borrowModelService.saveBorrow(borrowModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public final ResponseEntity<String> getBorrow(final String auth, final UUID borrowId) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final BorrowModel borrowModel = this.borrowModelService.getBorrowModelByUUID(borrowId);

        if(!requestedModel.isTeam() && !borrowModel.getUserId().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        return borrowModel.toResponse(this.mediaModelService).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getBorrowOfUser(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetModel = this.userModelService.getUserModelByUUID(uuid);

        if(!requestedModel.isTeam() && !requestedModel.getUuid().equals(targetModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        return BorrowSearchResponseModel.builder().borrows(this.borrowModelService.getBorrowsOfUser(targetModel)).build().toResponseEntity(HttpStatus.OK);

    }

    public final ResponseEntity<String> getBorrows(final String auth, final int page, final int size) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return BorrowSearchResponseModel.builder().borrows(this.borrowModelService.getBorrows(page, size)).count(this.borrowModelService.countAllBorrows()).build().toResponseEntity(HttpStatus.OK);

    }
}
