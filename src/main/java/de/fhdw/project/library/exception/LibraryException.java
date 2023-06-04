package de.fhdw.project.library.exception;

import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class LibraryException extends Exception{

    private final ErrorType errorType;

    public LibraryException(final ErrorType errorType){
        this.errorType = errorType;
    }

    public ResponseEntity<String> toResponseEntity(){
        return this.errorType.toResponseEntity();
    }
}
