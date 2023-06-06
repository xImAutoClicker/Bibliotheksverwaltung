package de.fhdw.project.library.media.service.request;

import ch.qos.logback.core.testUtil.RandomUtil;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadRequestModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadSearchResponseModel;
import de.fhdw.project.library.media.service.MediaHeadModelService;
import de.fhdw.project.library.media.service.MediaModelService;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.service.UserModelService;
import de.fhdw.project.library.util.RandomString;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
public class MediaHeadModelRequestService {

    @Autowired
    private UserModelService userModelService;
    @Autowired
    private MediaHeadModelService mediaHeadModelService;
    @Autowired
    private MediaModelService mediaModelService;
    public final ResponseEntity<String> getMediaHead(final String auth, final String isbn) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        final MediaHeadModel mediaHeadModel = this.mediaHeadModelService.getMediaHeadModelByISBN(isbn);
        return mediaHeadModel.toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> searchMediaHead(final String auth, final String filter, int page, final int size) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return MediaHeadSearchResponseModel.builder().mediaHead(this.mediaHeadModelService.searchMedia(mediaModelService, filter, page, size)).count(this.mediaHeadModelService.countAllMediaHead()).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> createMediaHead(final String auth, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);

        if(!userModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final MediaHeadRequestModel mediaHeadRequestModel = MediaHeadRequestModel.fromJsonWithError(body);

        if(mediaHeadRequestModel.getDescription() == null || mediaHeadRequestModel.getName() == null || mediaHeadRequestModel.getCover() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        // Books without ISBN gets a generated fake ISBN -> fake ISBN = String ISBN
        if(mediaHeadRequestModel.getIsbn() == null || mediaHeadRequestModel.getIsbn().length() < 1)
            mediaHeadRequestModel.setIsbn(new RandomString(3, new SecureRandom(), RandomString.alphanum).nextString() + "-" +new RandomString(10, new SecureRandom(), RandomString.alphanum).nextString());

        if(this.mediaHeadModelService.getMediaHeadModelByISBNWithOutError(mediaHeadRequestModel.getIsbn()) != null)
            throw new LibraryException(ErrorType.ISBN_ALREADY_IN_USE);

        return this.mediaHeadModelService.createModelHead(mediaHeadRequestModel.getName(), mediaHeadRequestModel.getIsbn(), mediaHeadRequestModel.getAuthor(), mediaHeadRequestModel.getDescription(),
                mediaHeadRequestModel.getReleaseDate(), mediaHeadRequestModel.getCover()).toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> editMediaHead(final String auth, final String isbn, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        if(!userModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final MediaHeadModel mediaModel = this.mediaHeadModelService.getMediaHeadModelByISBN(isbn);
        final MediaHeadRequestModel mediaRequestModel = MediaHeadRequestModel.fromJsonWithError(body);

        if(mediaRequestModel.getName() != null)
            this.mediaHeadModelService.editName(mediaModel, mediaRequestModel.getName());
        if(mediaRequestModel.getAuthor() != null)
            this.mediaHeadModelService.editAuthor(mediaModel, mediaRequestModel.getAuthor());
        if(mediaRequestModel.getDescription() != null)
            this.mediaHeadModelService.editDescription(mediaModel, mediaRequestModel.getDescription());
        if(mediaRequestModel.getCover() != null)
            this.mediaHeadModelService.editCover(mediaModel, mediaRequestModel.getCover());
        if(mediaRequestModel.getIsbn() != null)
            this.mediaHeadModelService.editIsbn(mediaModel, mediaRequestModel.getIsbn());
        if(mediaRequestModel.getReleaseDate() != null)
            this.mediaHeadModelService.editReleaseDate(mediaModel, mediaRequestModel.getReleaseDate());

        this.mediaHeadModelService.saveMediaHead(mediaModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public final ResponseEntity<String> deleteMediaHead(final String auth, final String isbn) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final MediaHeadModel mediaHeadModel = this.mediaHeadModelService.getMediaHeadModelByISBN(isbn);
        this.mediaHeadModelService.deleteMediaHead(mediaHeadModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
