package de.fhdw.project.library.media.service;

import de.fhdw.project.library.media.repository.ReservationModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationModelService {
    @Autowired
    private ReservationModelRepository reservationModelRepository;

    /**
     * Add - MediaId, UserId
     * Update Status - ReservationId - StatusCode (nach enum liste 0 = OPEN | 1 = ACCEPTED)
     */

    private UUID getFreeReservationUUID(){
        UUID uuid = UUID.randomUUID();
        while(this.reservationModelRepository.existsById(uuid))
            uuid = UUID.randomUUID();
        return uuid;
    }
}
