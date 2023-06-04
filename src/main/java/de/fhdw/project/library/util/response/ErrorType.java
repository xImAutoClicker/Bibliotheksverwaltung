package de.fhdw.project.library.util.response;

import de.fhdw.project.library.util.map.ExchangeMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public enum ErrorType {

    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    USERNAME_IS_ALREADY_IN_USE(501, "Username is already in use", HttpStatus.BAD_REQUEST),
    EMAIL_IS_ALREADY_IN_USE(502, "Email is already in use", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD_USERNAME(503, "Wrong password/username", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(504, "User not found", HttpStatus.BAD_REQUEST),
    INVALID_SESSION_TOKEN(505, "Invalid SessionToken", HttpStatus.UNAUTHORIZED),
    EMAIL_IS_NOT_IN_THE_RIGHT_FORMAT(506, "The Email is not in the right format", HttpStatus.BAD_REQUEST),
    USERNAME_LENGTH_INVALID(507, "The username length is invalid", HttpStatus.BAD_REQUEST),
    DOES_NOT_HAVE_PERMISSION(536, "Doesn't have permission for the function", HttpStatus.UNAUTHORIZED),
    MEDIA_NOT_FOUND(537, "Media not found", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String status;
    private HttpStatus httpStatus;

    public final ExchangeMap toMap(){
        return new ExchangeMap().put("code", this.code).put("status", this.status);
    }

    public final String toJson(){
        return this.toMap().toJson();
    }

    public final ResponseEntity<String> toResponseEntity(){
        return new ResponseEntity<>(this.toJson(), this.httpStatus);
    }

}
