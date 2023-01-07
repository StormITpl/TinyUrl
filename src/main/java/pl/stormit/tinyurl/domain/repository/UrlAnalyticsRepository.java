package pl.stormit.tinyurl.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;

import java.util.UUID;

@Repository
public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, UUID> {
}
