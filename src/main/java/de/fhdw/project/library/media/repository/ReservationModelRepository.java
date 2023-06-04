package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.reservation.ReservationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationModelRepository extends MongoRepository<ReservationModel, UUID> {
    List<ReservationModel> findReservationModelsByUserId(final UUID userId);
}
