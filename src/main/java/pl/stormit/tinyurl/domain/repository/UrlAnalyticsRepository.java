package pl.stormit.tinyurl.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;

import java.util.List;
import java.util.UUID;

@Repository
public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, UUID> {

    @Query("select max (u.totalClicks) from UrlAnalytics as u where u.url.id = :id")
    Long findMaxClickOnShortUrlByUrlId(UUID id);

    List<UrlAnalytics> findAllByUrlId(UUID id);

    @Query("SELECT u.shortUrl, a.totalClicks as totalClicks " +
            "FROM UrlAnalytics a " +
            "JOIN a.url u " +
            "GROUP BY u.shortUrl " +
            "ORDER BY totalClicks DESC " +
            "LIMIT 5")
    List<UrlAnalyticsDto[]> findMostPopularUrls();
}
