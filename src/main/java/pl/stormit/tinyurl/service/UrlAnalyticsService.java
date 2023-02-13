package pl.stormit.tinyurl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsMapper;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlAnalyticsService {

    public static final long FIRST_CLICK_ON_SHORT_URL = 1L;

    private final UrlAnalyticsRepository urlAnalyticsRepository;

    private final UrlAnalyticsMapper urlAnalyticsMapper;

    public List<UrlAnalytics> getAnalyticsByUrlId(UUID urlId) {
        if (!urlAnalyticsRepository.existsById(urlId)) {
            throw new ApiException("Change the request your id does not exist!");
        }
        return urlAnalyticsRepository.findAllByUrlId(urlId);
    }

    public List<UrlAnalytics> getAllAnalytics() {
        return urlAnalyticsRepository.findAll();
    }

    public UrlAnalyticsDto clickCounter(Url url) {

        UUID urlId = url.getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
        urlAnalytics.setUrl(url);

        return urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
    }

    private Long checkClicksAmountOnShortUrl(UUID urlId) {
        long incMaxClickValue;
        Long maxClickValue = urlAnalyticsRepository.findMaxClickOnShortUrlByUrlId(urlId);

        if (maxClickValue == null) {
            return FIRST_CLICK_ON_SHORT_URL;
        } else {
            incMaxClickValue = maxClickValue.longValue();
            incMaxClickValue++;
            return incMaxClickValue;
        }
    }
}
