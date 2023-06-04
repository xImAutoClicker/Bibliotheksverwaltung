package de.fhdw.project.library.user.repository;

import de.fhdw.project.library.user.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserModelRepository extends MongoRepository<UserModel, UUID> {
    UserModel findByEmail(final String email);
    Page<UserModel> findUserEntriesByFirstNameStartingWithIgnoreCase(final String firstName, final Pageable pageable);
    boolean existsByEmail(final String email);
}
