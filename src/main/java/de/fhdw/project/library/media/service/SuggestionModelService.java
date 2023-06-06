package de.fhdw.project.library.media.service;

import com.google.common.collect.Lists;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.media.head.MediaHeadResponseModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionModel;
import de.fhdw.project.library.media.model.suggestion.SuggestionResponseModel;
import de.fhdw.project.library.media.repository.SuggestionModelRepository;
import de.fhdw.project.library.user.model.UserModel;
import de.fhdw.project.library.util.LibraryUtil;
import de.fhdw.project.library.util.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SuggestionModelService {

    @Autowired
    private SuggestionModelRepository suggestionModelRepository;

    public final SuggestionModel getSuggestionModelByUUID(final UUID uuid) throws LibraryException {
        final Optional<SuggestionModel> suggestionModel = this.suggestionModelRepository.findById(uuid);
        if(suggestionModel.isEmpty())
            throw new LibraryException(ErrorType.RESERVATION_NOT_FOUND);
        return suggestionModel.get();
    }

    public final void saveSuggestion(final SuggestionModel suggestionModel){
        this.suggestionModelRepository.save(suggestionModel);
    }

    public final void deleteSuggestion(final SuggestionModel suggestionModel) {
        this.suggestionModelRepository.delete(suggestionModel);
    }

    public final long countALlSuggestion(){
        return this.suggestionModelRepository.count();
    }


    /**
     * Create - nameOfBook, isbn, userId;
     * Update - suggestionId, StatusCode (0 = OPEN, 1 = ACCPETED, 2 = DENIED)
     */
    public final SuggestionModel createSuggestion(final UserModel userModel, final String nameOfBook, final String isbn, String cover) throws LibraryException {
        final byte[] coverArr = LibraryUtil.checkAvatar(cover); //PERFORMANCE

        final SuggestionModel suggestionModel = SuggestionModel.builder()
                .uuid(this.getFreeSuggestionUUID())
                .userId(userModel.getUuid())
                .nameOfBook(nameOfBook)
                .isbn(isbn)
                .cover(coverArr)
                .createdAt(Instant.now().toEpochMilli())
                .statusType(SuggestionModel.SuggestionStatusType.OPEN)
                .build();
        this.saveSuggestion(suggestionModel);
        return suggestionModel;
    }

    public final SuggestionModel updateSuggestion(final SuggestionModel suggestionModel, final SuggestionModel.SuggestionStatusType suggestionStatusType){
        suggestionModel.setStatusType(suggestionStatusType);
        this.saveSuggestion(suggestionModel);
        return suggestionModel;
    }

    public final List<SuggestionResponseModel> getSuggestions(int page, final int size){
        final List<SuggestionResponseModel> toReturn = Lists.newArrayList();
        this.suggestionModelRepository.findAll(PageRequest.of(--page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        return toReturn;
    }

    public final List<SuggestionResponseModel> getSuggestionsOfUser(final UserModel userModel){
        final List<SuggestionResponseModel> toReturn = Lists.newArrayList();
        this.suggestionModelRepository.findSuggestionModelsByUserId(userModel.getUuid()).forEach(entry -> toReturn.add(entry.toResponse()));
        return toReturn;
    }

    private UUID getFreeSuggestionUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.suggestionModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }

    public final List<SuggestionResponseModel> searchSuggestion(final String filter, int page, final int size) {
        page--;
        final List<SuggestionResponseModel> toReturn = Lists.newArrayList();
        if(filter == null || filter.isBlank() || filter.isEmpty())
            this.suggestionModelRepository.findAllByOrderByStatusTypeDesc(PageRequest.of(page, size)).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        else
            this.suggestionModelRepository.findSuggestionModelsByNameOfBookStartingWithIgnoreCaseOrIsbnStartingWithIgnoreCaseAndStatusTypeOrderByStatusTypeDesc(filter, filter, filter, PageRequest.of(page, size), SuggestionModel.SuggestionStatusType.OPEN).getContent().forEach(entry -> toReturn.add(entry.toResponse()));
        return toReturn;
    }
}
