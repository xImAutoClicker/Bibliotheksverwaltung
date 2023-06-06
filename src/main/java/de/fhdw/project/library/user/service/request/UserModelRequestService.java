package de.fhdw.project.library.user.service.request;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadRequestModel;
import de.fhdw.project.library.media.model.reservation.ReservationModel;
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

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
public class UserModelRequestService {

    @Autowired
    private UserModelService userModelService;

    public final ResponseEntity<String> registerUser(final String body) throws LibraryException {
        final UserModelRequest userModelRequest = UserModelRequest.fromJsonWithError(body);
        if(userModelRequest.getEmail() == null || userModelRequest.getPassword() == null || userModelRequest.getFirstName() == null || userModelRequest.getLastName() == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return this.userModelService.registerUser(userModelRequest.getFirstName(), userModelRequest.getLastName(), userModelRequest.getEmail(), userModelRequest.getPassword(), userModelRequest.getCity(), userModelRequest.getStreet(), userModelRequest.getStreetNumber(), userModelRequest.getZipCode(), userModelRequest.getPhoneNumber()).toResponse(true).toResponseEntity(HttpStatus.CREATED);
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

        log.info("Output: " + UserModelSearchResponse.builder().user(this.userModelService.getUserEntries(requestedModel,filter, page, size)).count(this.userModelService.countAllUser()).build().toResponseEntity(HttpStatus.OK));

        return UserModelSearchResponse.builder().user(this.userModelService.getUserEntries(requestedModel,filter, page, size)).count(this.userModelService.countAllUser()).build().toResponseEntity(HttpStatus.OK);
    }

    public final ResponseEntity<String> editUser(final String auth, final UUID uuid, final String body) throws LibraryException {
        final UserModel userModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetModel = this.userModelService.getUserModelByUUID(uuid);

        if(!userModel.isTeam() && !userModel.getUuid().equals(targetModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        final UserModelRequest userModelRequest = UserModelRequest.fromJsonWithError(body);

        if(userModelRequest.getFirstName() != null)
            this.userModelService.editFirstName(targetModel, userModelRequest.getFirstName());
        if(userModelRequest.getLastName() != null)
            this.userModelService.editLastName(targetModel, userModelRequest.getLastName());
        if(userModelRequest.getCity() != null)
            this.userModelService.editCity(targetModel, userModelRequest.getCity());
        if(userModelRequest.getEmail() != null)
            this.userModelService.editEmail(targetModel, userModelRequest.getEmail());
        if(userModelRequest.getPhoneNumber() != null)
            this.userModelService.editPhoneNumber(targetModel, userModelRequest.getPhoneNumber());
        if(userModelRequest.getStreet() != null)
            this.userModelService.editStreet(targetModel, userModelRequest.getStreet());
        if(userModelRequest.getStreetNumber() != null)
            this.userModelService.editStreetNumber(targetModel, userModelRequest.getStreetNumber());
        if(userModelRequest.getZipCode() != null)
            this.userModelService.editZipCode(targetModel, userModelRequest.getZipCode());
        if(userModelRequest.getPassword() != null)
            this.userModelService.editPassword(targetModel, userModelRequest.getPassword());
        if(userModelRequest.getCover() != null)
            this.userModelService.editAvatar(targetModel, userModelRequest.getCover());
        if(userModelRequest.getTeam() != null)
            targetModel.setTeam(userModelRequest.getTeam());

        this.userModelService.saveUser(targetModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(String auth, UUID uuid) throws LibraryException {
        final UserModel requestedModel = this.userModelService.getUserModelByHeader(auth);
        final UserModel targetModel = this.userModelService.getUserModelByUUID(uuid);

        if(!requestedModel.isTeam() && !targetModel.getUuid().equals(requestedModel.getUuid()))
            throw new LibraryException(ErrorType.DOES_NOT_HAVE_PERMISSION);

        this.userModelService.deleteUser(targetModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
