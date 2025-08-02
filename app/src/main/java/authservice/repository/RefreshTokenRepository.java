package authservice.repository;

import org.springframework.stereotype.Repository;

import authservice.entities.RefreshToken;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

}
