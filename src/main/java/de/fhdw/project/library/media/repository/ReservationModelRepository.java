package de.fhdw.project.library.media.repository;

import de.fhdw.project.library.media.model.ReservationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ReservationModelRepository extends MongoRepository<ReservationModel, UUID> {
}
