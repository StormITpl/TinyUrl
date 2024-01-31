package pl.stormit.tinyurl.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.config.UrlAnalyticsConfig;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.mappers.UrlAnalyticsMapper;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.mappers.UrlMapper;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlAnalyticsService {

    public static final long FIRST_CLICK_ON_SHORT_URL = 1L;

    private final PageRequest amountOfPopularUrls;

    private final UrlAnalyticsRepository urlAnalyticsRepository;

    private final UrlRepository urlRepository;

    private final UrlAnalyticsMapper urlAnalyticsMapper;

    private final UrlMapper urlMapper;

    private final IpLocalizationService ipLocalization;

    @Transactional(readOnly = true)
    public List<UrlAnalyticsDto> getAnalyticsByUrlId(UUID urlId) {

        if (!urlAnalyticsRepository.existsById(urlId)) {
            throw new ApiException("The url by id: " + urlId + ", does not exist!");
        }
        return urlAnalyticsRepository.findAllByUrlId(urlId);
    }

    @Transactional(readOnly = true)
    public List<UrlAnalyticsDto> getAllAnalytics() {
        return urlAnalyticsRepository.findAll().stream()
                .map(urlAnalyticsMapper::mapUrlAnalyticsEntityToUrlAnalyticsDto)
                .toList();
    }

    public void setAnalyticsData(Url url, HttpServletRequest servletRequest) {

        String addressIp = servletRequest.getRemoteAddr();

//        UrlAnalyticsLocalizationDto analyticsLocalizationDto = ipLocalization.getIpLocalization(addressIp);

        UUID urlId = url.getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
//        urlAnalytics.setCountryLocalization(analyticsLocalizationDto.getCountryLocalization());
//        urlAnalytics.setIsoCode(analyticsLocalizationDto.getIsoCode());
//        urlAnalytics.setCityLocalization(analyticsLocalizationDto.getCityLocalization());
        urlAnalytics.setUrl(url);

        urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
    }

    private Long checkClicksAmountOnShortUrl(UUID urlId) {
        return Optional.ofNullable(urlAnalyticsRepository.findMaxClickOnShortUrlByUrlId(urlId))
                .map(maxClickValue -> maxClickValue + 1)
                .orElse(FIRST_CLICK_ON_SHORT_URL);
    }

    public List<UrlDto> findMostPopularUrls() {
        PageRequest pageRequest = AMOUNT_OF_POPULAR_URLS;
        List<MostPopularUrlResult> results = urlRepository.findMostPopularUrls(pageRequest);

        List<UrlDto> popularUrls = new ArrayList<>();
        for (MostPopularUrlResult result : results) {
            Url url = result.getUrl();
            Long totalClicks = result.getTotalClicks();
            UrlDto urlDto = urlMapper.mapUrlEntityToUrlDto(url);
            UrlAnalyticsDto analyticsDto = new UrlAnalyticsDto();
            analyticsDto.setTotalClicks(totalClicks);
            urlDto.setAnalytics(analyticsDto);
            popularUrls.add(urlDto);
        }

        return popularUrls;
    }
}
