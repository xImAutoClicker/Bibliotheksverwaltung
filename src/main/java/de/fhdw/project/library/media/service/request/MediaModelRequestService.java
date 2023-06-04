package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.MediaModel;
import de.fhdw.project.library.media.model.MediaSearchResponseModel;
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
public class MediaModelRequestService {

    @Autowired
    private MediaModelService mediaModelService;
    @Autowired
    private UserModelService userModelService;

    public final ResponseEntity<String> getMedia(final String auth, final UUID uuid) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        final MediaModel mediaModel = this.mediaModelService.getMediaModelByUUID(uuid);

        return mediaModel.toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> searchMedia(final String auth, final String filter, int page, final int size) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return MediaSearchResponseModel.builder().media(this.mediaModelService.searchMedia(filter, page, size)).count(this.mediaModelService.countAllMedia()).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> createMedia(final String auth, final String body) throws LibraryException {
        return null;
    }

    public final ResponseEntity<String> editMedia(final String auth, final String body) throws LibraryException {
        return null;
    }

    public final ResponseEntity<String> deleteMedia(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final MediaModel mediaModel = this.mediaModelService.getMediaModelByUUID(uuid);

        this.mediaModelService.deleteMedia(mediaModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
