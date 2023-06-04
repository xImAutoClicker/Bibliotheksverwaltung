package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadResponseModel;
import de.fhdw.project.library.media.repository.MediaHeadModelRepository;
import de.fhdw.project.library.util.LibraryUtil;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MediaHeadModelService {

    @Autowired
    private MediaHeadModelRepository mediaHeadModelRepository;

    public final MediaHeadModel getMediaHeadModelByISBN(final String isbn) throws LibraryException {
        final Optional<MediaHeadModel> MediaHeadModel = this.mediaHeadModelRepository.findById(isbn);
        if(MediaHeadModel.isEmpty())
            throw new LibraryException(ErrorType.MEDIA_HEAD_NOT_FOUND);
        return MediaHeadModel.get();
    }

    public final MediaHeadModel getMediaHeadModelByISBNWithOutError(final String isbn){
        final Optional<MediaHeadModel> MediaHeadModel = this.mediaHeadModelRepository.findById(isbn);
        return MediaHeadModel.orElse(null);
    }

    public final void saveMediaHead(final MediaHeadModel MediaHeadModel){
        this.mediaHeadModelRepository.save(MediaHeadModel);
    }

    public final void deleteMediaHead(final MediaHeadModel MediaHeadModel) {
        this.mediaHeadModelRepository.delete(MediaHeadModel);
    }

    public final long countAllMediaHead(){
        return this.mediaHeadModelRepository.count();
    }

    public final List<MediaHeadResponseModel> searchMedia(final String filter, int page, int size) throws LibraryException {
        page--;
        final List<MediaHeadResponseModel> toReturn = Lists.newArrayList();
        if(filter == null || filter.isBlank() || filter.isEmpty())
            this.mediaHeadModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        else
            this.mediaHeadModelRepository.findMediaHeadModelsByNameStartingWithIgnoreCaseOrIsbnStartingWithIgnoreCaseOrAuthorStartingWithIgnoreCase(filter, filter, filter, PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        return toReturn;
    }

    public final MediaHeadModel createModelHead(final String name, final String isbn, final String author, final String description, final LocalDate releaseDate, final String cover) throws LibraryException {
        //TODO: ADD CHECK
        final byte[] coverArr = LibraryUtil.checkAvatar(cover); //PERFORMANCE
        final MediaHeadModel mediaHeadModel = MediaHeadModel.builder()
                .isbn(isbn)
                .name(name)
                .author(author)
                .description(description)
                .releaseDate(releaseDate)
                .cover(coverArr)
                .build();
        this.saveMediaHead(mediaHeadModel);
        return mediaHeadModel;
    }

    public final MediaHeadModel editName(final MediaHeadModel MediaHeadModel, final String name){
        return this.editName(MediaHeadModel, name, false);
    }

    public final MediaHeadModel editName(final MediaHeadModel MediaHeadModel, final String name, final boolean save){
        MediaHeadModel.setName(name);
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }

    public final MediaHeadModel editIsbn(final MediaHeadModel MediaHeadModel, final String isbn){
        return this.editIsbn(MediaHeadModel, isbn, false);
    }

    public final MediaHeadModel editIsbn(final MediaHeadModel MediaHeadModel, final String isbn, final boolean save){
        MediaHeadModel.setIsbn(isbn);
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }

    public final MediaHeadModel editDescription(final MediaHeadModel MediaHeadModel, final String description){
        return this.editDescription(MediaHeadModel, description, false);
    }

    public final MediaHeadModel editDescription(final MediaHeadModel MediaHeadModel, final String description, final boolean save){
        MediaHeadModel.setDescription(description);
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }

    public final MediaHeadModel editReleaseDate(final MediaHeadModel MediaHeadModel, final LocalDate releaseDate){
        return this.editReleaseDate(MediaHeadModel, releaseDate, false);
    }

    public final MediaHeadModel editReleaseDate(final MediaHeadModel MediaHeadModel, final LocalDate releaseDate, final boolean save){
        MediaHeadModel.setReleaseDate(releaseDate);
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }


    public final MediaHeadModel editAuthor(final MediaHeadModel MediaHeadModel, final String author) throws LibraryException{
        return this.editAuthor(MediaHeadModel, author, false);
    }

    public final MediaHeadModel editAuthor(final MediaHeadModel MediaHeadModel, final String author, final boolean save) throws LibraryException{
        MediaHeadModel.setAuthor(author);
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }

    public final MediaHeadModel editCover(final MediaHeadModel MediaHeadModel, final String cover) throws LibraryException{
        return this.editCover(MediaHeadModel, cover, false);
    }

    public final MediaHeadModel editCover(final MediaHeadModel MediaHeadModel, final String cover, final boolean save) throws LibraryException{
        MediaHeadModel.setCover(LibraryUtil.checkAvatar(cover));
        if(save)
            this.saveMediaHead(MediaHeadModel);
        return MediaHeadModel;
    }
}
