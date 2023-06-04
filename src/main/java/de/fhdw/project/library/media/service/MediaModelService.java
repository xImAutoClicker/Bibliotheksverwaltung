package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.repository.MediaModelRepository;
import de.fhdw.project.library.util.LibraryUtil;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Getter
public class MediaModelService {

    @Autowired
    private MediaModelRepository mediaModelRepository;
    @Autowired
    private MediaHeadModelService mediaHeadModelService;

    public final MediaModel getMediaModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<MediaModel> mediaModel = this.mediaModelRepository.findById(uuid);
        if(mediaModel.isEmpty())
            throw new LibraryException(ErrorType.MEDIA_NOT_FOUND);
        return mediaModel.get();
    }

    public final MediaModel getMediaModelByUUIDWithOutError(final UUID uuid){
        final Optional<MediaModel> mediaModel = this.mediaModelRepository.findById(uuid);
        return mediaModel.orElse(null);
    }

    public final void saveMedia(final MediaModel mediaModel){
        this.mediaModelRepository.save(mediaModel);
    }

    public final void deleteMedia(final MediaModel mediaModel) {
        this.mediaModelRepository.delete(mediaModel);
    }

    public final long countAllMedia(){
        return this.mediaModelRepository.count();
    }

    public final List<MediaResponseModel> searchMedia(final String filter, int page, int size) throws LibraryException {
        page--;
        final List<MediaResponseModel> toReturn = Lists.newArrayList();
        if(filter == null || filter.isBlank() || filter.isEmpty())
            this.mediaModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(mediaHeadModelService)));
        else
            this.mediaModelRepository.findMediaModelsByIsbnStartingWithIgnoreCase(filter, PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(mediaHeadModelService)));
        return toReturn;
    }

    public final MediaModel createModel(final MediaHeadModel mediaHeadModel) throws LibraryException {
        final MediaModel mediaModel = MediaModel.builder()
                .uuid(this.getFreeUserEntryUUID())
                .isbn(mediaHeadModel.getIsbn())
                .build();
        this.saveMedia(mediaModel);
        return mediaModel;
    }

    private UUID getFreeUserEntryUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.mediaModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
