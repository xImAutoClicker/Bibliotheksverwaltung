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
    USERNAME_IS_ALREADY_IN_USE(501, "Benutzername wird bereits verwendet!", HttpStatus.BAD_REQUEST),
    EMAIL_IS_ALREADY_IN_USE(502, "Diese E-Mail wird bereits benutzt!", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD_USERNAME(503, "Falsches Passwort / falsche E-Mail!", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(504, "Benutzer wurde nicht gefunden!", HttpStatus.BAD_REQUEST),
    INVALID_SESSION_TOKEN(505, "Invalid SessionToken", HttpStatus.UNAUTHORIZED),
    EMAIL_IS_NOT_IN_THE_RIGHT_FORMAT(506, "Die E-Mail ist im falschen Format!", HttpStatus.BAD_REQUEST),
    USERNAME_LENGTH_INVALID(507, "Der Name ist zu lang!", HttpStatus.BAD_REQUEST),
    DOES_NOT_HAVE_PERMISSION(536, "Sie haben keinen Zugriff auf diese Funktion!", HttpStatus.UNAUTHORIZED),
    MEDIA_NOT_FOUND(537, "Es konnte kein freies Buch gefunden werden! Sie haben die Möglichkeit das Buch zu reservieren.", HttpStatus.BAD_REQUEST),
    IMAGE_IS_NOT_IN_THE_RIGHT_FORMAT(538, "Das Bild ist nicht im richtigen Format!", HttpStatus.BAD_REQUEST),
    IMAGE_IS_TOO_LARGE(539, "Das Bild ist zu groß!", HttpStatus.BAD_REQUEST),
    RESERVATION_NOT_FOUND(540, "Reservierung wurde nicht gefunden!", HttpStatus.BAD_REQUEST),
    MEDIA_HEAD_NOT_FOUND(541, "Der Buch-Kopf wurde nicht gefunden!", HttpStatus.BAD_REQUEST),
    ISBN_ALREADY_IN_USE(542, "Die ISBN wurde bereits verwendet. Ist das Buch schon vorhanden?", HttpStatus.BAD_REQUEST),
    MEDIA_FOUND_BUT_NO_ITEMS_CREATED(543, "Das Buch wurde zwar gefunden, es wurde jedoch kein Bestand gefunden!", HttpStatus.BAD_REQUEST),
    RESERVATION_WAS_CREATED(544, "Das Buch ist aktuell nicht verfügbar. Wir haben das Buch für Sie reserviert.", HttpStatus.BAD_REQUEST);

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
