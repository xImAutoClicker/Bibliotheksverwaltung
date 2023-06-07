package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.borrow.BorrowModel;
import de.fhdw.project.library.media.model.media.MediaModel;
import de.fhdw.project.library.media.model.media.MediaResponseModel;
import de.fhdw.project.library.media.model.media.head.MediaHeadModel;
import de.fhdw.project.library.media.repository.BorrowModelRepository;
import de.fhdw.project.library.media.repository.MediaModelRepository;
import de.fhdw.project.library.util.LibraryUtil;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public final void deleteMedia(final MediaModel mediaModel, final BorrowModelService borrowModelService) throws LibraryException{
        if(borrowModelService.existsBorrowFromUUID(mediaModel.getUuid(), BorrowModel.BorrowStatusType.OPEN))
            throw new LibraryException(ErrorType.MEDIA_IS_IN_USE);
        this.mediaModelRepository.delete(mediaModel);
    }

    public final long countAllMedia(){
        return this.mediaModelRepository.count();
    }

    public final List<MediaResponseModel> searchMedia(final String filter, int page, int size) throws LibraryException {
        page--;
        final List<MediaResponseModel> toReturn = Lists.newArrayList();
        if(filter == null || filter.isBlank() || filter.isEmpty())
            this.mediaModelRepository.findAll(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(this.mediaHeadModelService)));
        else
            this.mediaModelRepository.findMediaModelsByIsbnStartingWithIgnoreCase(filter, PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse(this.mediaHeadModelService)));
        return toReturn;
    }

    public final List<MediaResponseModel> getAllMediaFromMediaHead(final MediaHeadModel mediaHeadModel) throws LibraryException {
        final List<MediaResponseModel> toReturn = Lists.newArrayList();

        final List<MediaModel> content = this.mediaModelRepository.findMediaModelsByIsbn(mediaHeadModel.getIsbn());

        if(!content.isEmpty())
            content.forEach(entry -> toReturn.add(entry.toResponse(this.mediaHeadModelService)));

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

    public final int getAmountOfBooks(final String isbn) {
        return this.mediaModelRepository.countAllByIsbn(isbn);
    }

    public final List<MediaModel> getMediaModelsByISBN(final String isbn) {
        return this.mediaModelRepository.findMediaModelsByIsbn(isbn);
    }

    public final MediaModel getFreeMedia(final String isbn, BorrowModelService borrowModelService) throws LibraryException {
        List<MediaModel> mediaModelList = this.getMediaModelsByISBN(isbn);

        if(mediaModelList == null || mediaModelList.isEmpty())
            throw new LibraryException(ErrorType.MEDIA_FOUND_BUT_NO_ITEMS_CREATED);

        List<BorrowModel> borrowModelList = borrowModelService.getBorrowsFromIsbn(isbn, BorrowModel.BorrowStatusType.OPEN);
        if(borrowModelList.isEmpty())
            return mediaModelList.get(new Random().nextInt(mediaModelList.size()));

        for(MediaModel mediaModel : mediaModelList) {
            if(borrowModelService.existsBorrowFromUUID(mediaModel.getUuid(), BorrowModel.BorrowStatusType.OPEN))continue;
            return mediaModel;
        }

        return null;
    }

    public final BorrowModel getNextFreeMedia(final String isbn, BorrowModelService borrowModelService) throws LibraryException {
        List<MediaModel> mediaModelList = this.getMediaModelsByISBN(isbn);

        if(mediaModelList == null || mediaModelList.isEmpty())
            throw new LibraryException(ErrorType.MEDIA_FOUND_BUT_NO_ITEMS_CREATED);

        List<BorrowModel> borrowModelList = borrowModelService.getBorrowsFromIsbn(isbn);

        BorrowModel bestBorrowModel = null;
        for(BorrowModel borrowModel : borrowModelList) {
            if(bestBorrowModel == null || borrowModel.getBorrowEnd() < bestBorrowModel.getBorrowEnd())
                bestBorrowModel = borrowModel;
        }
        return bestBorrowModel;
    }

    public long getEstimatedDelivery(String isbn, BorrowModelService borrowModelService) {
        BorrowModel bestBorrowModel = null;

        try {
            bestBorrowModel = this.getNextFreeMedia(isbn, borrowModelService);
        } catch (Exception ignore) {

        }
        return bestBorrowModel == null ? 0 : bestBorrowModel.getBorrowEnd();
    }
}
