package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.head.MediaHeadSearchResponseModel;
import de.fhdw.project.library.media.model.reservation.ReservationModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionRequestModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionSearchResponseModel;
import de.fhdw.project.library.media.service.SuggestionModelService;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.service.UserModelService;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class SuggestionModelRequestService {

    @Autowired
    private SuggestionModelService suggestionModelService;

    @Autowired
    private UserModelService userModelService;

    public final ResponseEntity<String> createSuggestion(final String auth, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        final SuggestionRequestModel suggestionRequestModel = SuggestionRequestModel.fromJsonWithError(body);

        if(suggestionRequestModel.getNameOfBook() == null || suggestionRequestModel.getSuggestionStatusType() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return this.suggestionModelService.createSuggestion(userModel, suggestionRequestModel.getNameOfBook(), suggestionRequestModel.getIsbn(), suggestionRequestModel.getCover()).toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> updateSuggestion(final String auth, final UUID uuid, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        
        if(!userModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final SuggestionModel suggestionModel = this.suggestionModelService.getSuggestionModelByUUID(uuid);
        final SuggestionRequestModel suggestionRequestModel = SuggestionRequestModel.fromJsonWithError(body);

        this.suggestionModelService.updateSuggestion(suggestionModel, suggestionRequestModel.getSuggestionStatusType());
        this.suggestionModelService.saveSuggestion(suggestionModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public final ResponseEntity<String> getSuggestion(final String auth, final UUID suggestionId) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final SuggestionModel suggestionModel = this.suggestionModelService.getSuggestionModelByUUID(suggestionId);
        
        if(!requestedModel.isTeam() && !suggestionModel.getUserId().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        
        return suggestionModel.toResponse().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getSuggestionUser(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetModel = this.userModelService.getUserModelByUUID(uuid);
        
        if(!requestedModel.isTeam() && !requestedModel.getUuid().equals(targetModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        
        return SuggestionSearchResponseModel.builder().suggestions(this.suggestionModelService.getSuggestionsOfUser(targetModel)).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getSuggestions(final String auth, final int page, final int size) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return SuggestionSearchResponseModel.builder().suggestions(this.suggestionModelService.getSuggestions(page, size)).count(this.suggestionModelService.countALlSuggestion()).build().toResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<String> searchSuggestion(String auth, String filter, int page, int size) throws LibraryException {
        this.userModelService.getUserModelByHeader(auth);

        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        return SuggestionSearchResponseModel.builder().suggestions(this.suggestionModelService.searchSuggestion(filter, page, size)).count(this.suggestionModelService.countALlSuggestion()).build().toResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<String> deleteSuggestion(String auth, UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final SuggestionModel reservationModel = this.suggestionModelService.getSuggestionModelByUUID(uuid);

        if(!requestedModel.isTeam() && !reservationModel.getUserId().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        this.suggestionModelService.deleteSuggestion(reservationModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
