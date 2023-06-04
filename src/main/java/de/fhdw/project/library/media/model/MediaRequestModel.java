package de.fhdw.project.library.media.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MediaRequestModel {
    private String name;
    private String isbn;
    private String author;
    private String description;
    private LocalDate releaseDate;
    private String cover;
}
