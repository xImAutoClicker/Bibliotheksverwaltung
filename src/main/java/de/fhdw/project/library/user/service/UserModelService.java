package de.fhdw.project.library.user.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.user.model.UserModelResponse;
import de.fhdw.project.library.user.repository.UserModelRepository;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserModelService {

    @Autowired
    private UserModelRepository userModelRepository;

    public final UserModel getUserModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<UserModel> userModel = this.userModelRepository.findById(uuid);
        if(userModel.isEmpty())
            throw new LibraryException(ErrorType.USER_NOT_FOUND);
        return userModel.get();
    }

    public final void saveUser(final UserModel userModel){
        this.userModelRepository.save(userModel);
    }

    public final UserModel getUserModelByEmail(final String email){
        return this.userModelRepository.findByEmail(email);
    }

    private void checkMail(final String email) throws LibraryException {
        if(this.exitsUserModelEMail(email))
            throw new LibraryException(ErrorType.EMAIL_IS_ALREADY_IN_USE);
        if(email.length() > 128 || !EmailValidator.getInstance()
                .isValid(email))throw new LibraryException(ErrorType.EMAIL_IS_NOT_IN_THE_RIGHT_FORMAT);
    }

    public final boolean exitsUserModelEMail(final String email){
        return this.userModelRepository.existsByEmail(email);
    }

    public final UserModel getUserModelByHeader(final String auth) throws LibraryException {
        if(!auth.contains(":"))
            throw new LibraryException(ErrorType.INVALID_SESSION_TOKEN);
        final String[] data = auth.split(":");
        UUID uuid;
        try {
            uuid = UUID.fromString(data[0]);
        }catch (Exception e){
            throw new LibraryException(ErrorType.INVALID_SESSION_TOKEN);
        }
        final UserModel userModel = this.getUserModelByUUID(uuid);
        if(!userModel.getSessionToken().equals(data[1]))
            throw new LibraryException(ErrorType.INVALID_SESSION_TOKEN);
        return userModel;
    }

    public final long countAllUser(){
        return this.userModelRepository.count();
    }

    /**
     * Register
     * Login
     * GetUser
     */

    public final UserModel registerUser(final String firstName, final String lastName, final String email, final String password, final String city, final String street, final String streetNumber, final String zipCode) throws LibraryException {
        this.checkMail(email);
        final UserModel userEntry = UserModel.builder()
                .uuid(this.getFreeUserEntryUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .city(city)
                .street(street)
                .streetNumber(streetNumber)
                .zipCode(zipCode)
                .build()
                .generateSessionToken();

        log.info("User-Register => UUID:" + userEntry.getUuid());
        this.saveUser(userEntry);
        return userEntry;
    }

    public final UserModel loginUser(final String loginName, final String password) throws LibraryException {
        final UserModel userEntry = this.getUserModelByEmail(loginName);
        if(userEntry == null)
            throw new LibraryException(ErrorType.WRONG_PASSWORD_USERNAME);

        if(!userEntry.getPassword().equals(password))
            throw new LibraryException(ErrorType.WRONG_PASSWORD_USERNAME);

        userEntry.generateSessionToken();
        this.saveUser(userEntry);
        log.info("User-Login => UUID:" + userEntry.getUuid());
        return userEntry;
    }

    public final List<UserModelResponse> getUserEntries(final UserModel sender, final String filter, int page, final int size){
        page--;
        final List<UserModelResponse> toReturn = Lists.newArrayList();
        if(filter == null || filter.isEmpty() || filter.isBlank())
            this.userModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(sender)));
        else
            this.userModelRepository.findUserEntriesByFirstNameStartingWithIgnoreCase(filter, PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(sender)));
        return toReturn;
    }

    private List<UserModelResponse> getUserEntries(final UserModel sender, final int page, final int size){
        return this.getUserEntries(sender, "", page, size);
    }

    private UUID getFreeUserEntryUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.userModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }

}