package de.fhdw.project.library.user.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.model.UserModelRequest;
import de.fhdw.project.library.user.model.UserModelSearchResponse;
import de.fhdw.project.library.user.service.UserModelService;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserModelRequestService {

    @Autowired
    private UserModelService userModelService;

    public final ResponseEntity<String> registerUser(final String body) throws LibraryException {
        final UserModelRequest userModelRequest = UserModelRequest.fromJsonWithError(body);
        if(userModelRequest.getEmail() == null || userModelRequest.getPassword() == null || userModelRequest.getFirstName() == null || userModelRequest.getLastName() == null || userModelRequest.getCity() == null || userModelRequest.getStreet() == null || userModelRequest.getStreetNumber() == null || userModelRequest.getZipCode() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return this.userModelService.registerUser(userModelRequest.getFirstName(), userModelRequest.getLastName(), userModelRequest.getEmail(), userModelRequest.getPassword(), userModelRequest.getCity(), userModelRequest.getStreet(), userModelRequest.getStreetNumber(), userModelRequest.getZipCode()).toResponse(true).toResponseEntity(HttpStatus.CREATED);
    }

    public final ResponseEntity<String> loginUser(final String body) throws LibraryException {
        final UserModelRequest userModelRequest = UserModelRequest.fromJsonWithError(body);
        if(userModelRequest.getEmail() == null || userModelRequest.getPassword() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return this.userModelService.loginUser(userModelRequest.getEmail(), userModelRequest.getPassword()).toResponse(true).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getUser(final String auth, final UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetUser = this.userModelService.getUserModelByUUID(uuid);
        return targetUser.toResponse(requestedModel).toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> getUserList(final String auth, final String filter, final int page, final int size) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        if (page - 1 < 0 || size <= 0)
            throw new LibraryException(ErrorType.BAD_REQUEST);

        if(!requestedModel.isTeam())
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);
        return UserModelSearchResponse.builder().user(this.userModelService.getUserEntries(requestedModel,filter, page, size)).count(this.userModelService.countAllUser()).build().toResponseEntity(HttpStatus.OK);
    }
}
