package de.fhdw.project.library.user.model;

import de.fhdw.project.library.LibraryApplication;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;

@Getter
public class UserModelRequest {

    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String streetNumber;
    private String zipCode;
    private String phoneNumber;
    private Boolean team;
    private String cover;

    public static UserModelRequest fromJson(String json){
        try{
            return LibraryApplication.getGson().fromJson(json, UserModelRequest.class);
        }catch (Exception e){
            return null;
        }
    }

    public static UserModelRequest fromJsonWithError(final String json) throws LibraryException {
        final UserModelRequest userEntryRequest = fromJson(json);
        if(userEntryRequest == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return userEntryRequest;
    }

}
