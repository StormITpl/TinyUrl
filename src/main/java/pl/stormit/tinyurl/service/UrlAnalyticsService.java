package pl.stormit.tinyurl.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsMapper;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.dto.UrlMapper;
import pl.stormit.tinyurl.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlAnalyticsService {

    public static final long FIRST_CLICK_ON_SHORT_URL = 1L;

    private final UrlAnalyticsRepository urlAnalyticsRepository;

    private final UrlRepository urlRepository;

    private final UrlAnalyticsMapper urlAnalyticsMapper;

    private final UrlMapper urlMapper;

    private IpLocalizationService ipLocalization;

    public List<UrlAnalytics> getAnalyticsByUrlId(UUID urlId) {
        if (!urlAnalyticsRepository.existsById(urlId)) {
            throw new ApiException("Change the request your id does not exist!");
        }
        return urlAnalyticsRepository.findAllByUrlId(urlId);
    }

    public List<UrlAnalytics> getAllAnalytics() {
        return urlAnalyticsRepository.findAll();
    }

    public void setAnalitycsData(Url url, HttpServletRequest servletRequest) {

        String addressIp = servletRequest.getRemoteAddr();

        UrlAnalyticsLocalizationDto analyticsLocalizationDto = ipLocalization.getIpLocalization(addressIp);

        UUID urlId = url.getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
        urlAnalytics.setCountryLocalization(analyticsLocalizationDto.getCountryLocalization());
        urlAnalytics.setIsoCode(analyticsLocalizationDto.getIsoCode());
        urlAnalytics.setCityLocalization(analyticsLocalizationDto.getCityLocalization());
        urlAnalytics.setUrl(url);

        urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
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

    public List<UrlDto> findMostPopularUrls() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Object[]> results = urlRepository.findMostPopularUrls(pageRequest);

        List<UrlDto> popularUrls = new ArrayList<>();
        for (Object[] result : results) {
            Url url = (Url) result[0];
            Long totalClicks = (Long) result[1];
            UrlDto urlDto = urlMapper.mapUrlEntityToUrlDto(url);
            UrlAnalyticsDto analyticsDto = new UrlAnalyticsDto();
            analyticsDto.setTotalClicks(totalClicks);
            urlDto.setAnalytics(analyticsDto);
            popularUrls.add(urlDto);
        }

        return popularUrls;
    }
}
