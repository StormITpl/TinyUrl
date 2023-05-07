package pl.stormit.tinyurl.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.Url;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    Optional<Url> findUrlByShortUrl (String shortUrl);
    Optional<Url> findUrlByLongUrl (String longUrl);
}
