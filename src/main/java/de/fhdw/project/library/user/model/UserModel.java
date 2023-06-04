package de.fhdw.project.library.user.model;

import de.fhdw.project.library.util.RandomString;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("col_user")
public class UserModel {

    /**
     *
     *
     */
    @Id
    private UUID uuid;

    @Indexed
    private String firstName;

    @Indexed
    private String lastName;

    @Indexed
    private String email;

    private String password;

    private boolean team;

    private String sessionToken;

    private long createdAt;

    /**
     * Address of user
     */
    private String city;

    private String street;

    private String streetNumber;

    private String zipCode;


    /**
     * Generate a SessionToken as a String of 64 char's.
     * After the call you have to save the user.
     */
    public final UserModel generateSessionToken(){
        this.sessionToken = new RandomString(64, new SecureRandom(), RandomString.alphanum).nextString();
        return this;
    }

    public final UserModelResponse toResponse(final UserModel requested){
        return this.toResponse(requested,false);
    }

    public final UserModelResponse toResponse(){
        return this.toResponse(this,false);
    }

    public final UserModelResponse toResponse(final boolean withSession){
        return this.toResponse(this, withSession);
    }

    private UserModelResponse toResponse(final UserModel requested, final boolean withSession){
        final boolean isAllowed = requested.isTeam() || requested.uuid.equals(this.uuid);
        final UserModelResponse.UserModelResponseBuilder userModelResponseBuilder = UserModelResponse.builder()
                .uuid(this.uuid)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(isAllowed ? this.email : null)
                .city(isAllowed ? this.city : null)
                .street(isAllowed ? this.street : null)
                .streetNumber(isAllowed ? this.streetNumber : null)
                .zipCode(isAllowed ? this.zipCode : null);
        if(withSession)
            userModelResponseBuilder.sessionToken(this.sessionToken);
        return userModelResponseBuilder.build();
    }

}
