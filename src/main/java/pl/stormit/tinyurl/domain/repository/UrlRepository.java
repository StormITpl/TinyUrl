package pl.stormit.tinyurl.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.Url;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    Optional<Url> findUrlByShortUrl (String shortUrl);
    Optional<Url> findUrlByLongUrl (String longUrl);

    @Query("SELECT u FROM Url u JOIN u.urlAnalytics ua GROUP BY u.id, u.shortUrl, u.longUrl ORDER BY MAX(ua.totalClicks) DESC")
    List<Url> findMostPopularUrls(Pageable pageable);
}
