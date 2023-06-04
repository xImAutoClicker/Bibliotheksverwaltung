package de.fhdw.project.library.media.service;

import de.fhdw.project.library.media.repository.BorrowModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BorrowModelService {

    @Autowired
    private BorrowModelRepository borrowModelRepository;

    /**
     * Ausleihen: BorrowMedia (MediaID,UserID)
     * Rückgabe: ReBorrowMedia (MediaID,UserID)
     */

    private UUID getFreeBurrowModelId(){
        UUID uuid = UUID.randomUUID();
        while(this.borrowModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
