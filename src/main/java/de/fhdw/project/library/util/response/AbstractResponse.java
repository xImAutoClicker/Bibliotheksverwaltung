package de.fhdw.project.library.util.response;

import de.fhdw.project.library.LibraryApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractResponse {
    public String toJson() {
        return LibraryApplication.getGson().toJson(this);
    }

    public ResponseEntity<String> toResponseEntity(HttpStatus httpStatus) {
        return new ResponseEntity(toJson(), httpStatus);
    }
}

