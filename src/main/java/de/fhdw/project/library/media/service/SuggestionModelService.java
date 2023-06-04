package de.fhdw.project.library.media.service;

import de.fhdw.project.library.media.repository.SuggestionModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SuggestionModelService {

    @Autowired
    private SuggestionModelRepository suggestionModelRepository;

    /**
     * Create - nameOfBook, isbn, userId;
     * Update - suggestionId, StatusCode (0 = OPEN, 1 = ACCPETED, 2 = DENIED)
     */
    private UUID getFreeSuggestionUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.suggestionModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
