package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadRequestModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadSearchResponseModel;
import de.fhdw.project.library.media.service.MediaHeadModelService;
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
public class MediaHeadModelRequestService {

    @Autowired
    private MediaModelService mediaModelService;
    @Autowired
    private UserModelService userModelService;
    @Autowired
    private MediaHeadModelService mediaHeadModelService;

    public final ResponseEntity<String> getMediaHead(final String auth, final String isbn) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        final MediaHeadModel mediaHeadModel = this.mediaHeadModelService.getMediaHeadModelByISBN(isbn);
        return mediaHeadModel.toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> searchMediaHead(final String auth, final String filter, int page, final int size) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return MediaHeadSearchResponseModel.builder().mediaHead(this.mediaHeadModelService.searchMedia(filter, page, size)).count(this.mediaHeadModelService.countAllMediaHead()).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> createMediaHead(final String auth, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        if(!userModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final MediaHeadRequestModel mediaHeadRequestModel = MediaHeadRequestModel.fromJsonWithError(body);

        if(mediaHeadRequestModel.getAuthor() == null || mediaHeadRequestModel.getDescription() == null || mediaHeadRequestModel.getName() == null || mediaHeadRequestModel.getCover() == null || mediaHeadRequestModel.getIsbn() == null
                || mediaHeadRequestModel.getReleaseDate() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);

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
