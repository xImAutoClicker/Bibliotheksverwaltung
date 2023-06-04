package de.fhdw.project.library.exception;

import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.http.ResponseEntity;

public class LibraryException extends Exception{

    private final ErrorType errorType;

    public LibraryException(final ErrorType errorType){
        this.errorType = errorType;
    }

    public ResponseEntity<String> toResponseEntity(){
        return this.errorType.toResponseEntity();
    }
}
