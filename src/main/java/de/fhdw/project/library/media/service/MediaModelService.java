package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.MediaModel;
import de.fhdw.project.library.media.model.MediaResponseModel;
import de.fhdw.project.library.media.repository.MediaModelRepository;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaModelService {

    @Autowired
    private MediaModelRepository mediaModelRepository;

    public final MediaModel getMediaModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<MediaModel> mediaModel = this.mediaModelRepository.findById(uuid);
        if(mediaModel.isEmpty())
            throw new LibraryException(ErrorType.MEDIA_NOT_FOUND);
        return mediaModel.get();
    }

    public final long countAllMedia(){
        return this.mediaModelRepository.count();
    }

    public final List<MediaResponseModel> searchMedia(final String filter, int page, int size) throws LibraryException {
        page--;
        final List<MediaResponseModel> toReturn = Lists.newArrayList();
        if(filter == null || filter.isBlank() || filter.isEmpty())
            this.mediaModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        else
            this.mediaModelRepository.findMediaModelsByNameStartingWithIgnoreCase(filter, PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        return toReturn;
    }

    public final MediaModel createModel(){
        return null;
    }

    /**
     * GetMedia - Liste - (Name,ISB,Author)
     *
     * ADD Media - Name, ISB, Author, Description, LocalDate
     * EDIT Media
     * Delete Media - ID
     */

    public void deleteMedia(final MediaModel mediaModel) {
        this.mediaModelRepository.delete(mediaModel);
    }

    private UUID getFreeUserEntryUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.mediaModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
