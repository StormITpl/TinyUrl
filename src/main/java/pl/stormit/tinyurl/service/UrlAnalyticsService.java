package pl.stormit.tinyurl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlAnalyticsService {

    public static final long FIRST_CLICK_ON_SHORT_URL = 1L;

    private final UrlAnalyticsRepository urlAnalyticsRepository;

    public List<UrlAnalytics> getAnalyticsByUrlId(UUID urlId) {
        return urlAnalyticsRepository.findAllByUrlId(urlId);
    }

    public List<UrlAnalytics> getAllAnalytics() {
        return urlAnalyticsRepository.findAll();
    }

    public UrlAnalytics clickCounter(Optional<Url> url) {

        UUID urlId = url.get().getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
        urlAnalytics.setUrl(url.get());

        return urlAnalyticsRepository.save(urlAnalytics);
    }

    private Long checkClicksAmountOnShortUrl(UUID urlId) {
        Long incMaxClickValue;
        Optional<Long> maxClickValue = urlAnalyticsRepository.findAllByUrlId(urlId).stream()
                .map(UrlAnalytics::getTotalClicks)
                .collect(Collectors.maxBy(Comparator.naturalOrder()));

        if (maxClickValue.isPresent()) {
            incMaxClickValue = maxClickValue.get().longValue();
            incMaxClickValue++;
        } else {
            return FIRST_CLICK_ON_SHORT_URL;
        }
        return incMaxClickValue;
    }
}
