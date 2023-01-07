package pl.stormit.tinyurl.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.UrlExpiry;

import java.util.UUID;

@Repository
public interface UrlExpiryRepository extends JpaRepository<UrlExpiry, UUID> {
}
