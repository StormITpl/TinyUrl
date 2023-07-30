package pl.stormit.tinyurl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles({"test"})
@SpringBootTest
@Transactional
class UrlAnalyticsServiceTest {

    @Autowired
    private UrlAnalyticsService urlAnalyticsService;

    @Autowired
    private IpLocalizationService ipLocalizationService;

    @Autowired
    private UrlAnalyticsRepository urlAnalyticsRepository;

    @Autowired
    private UrlRepository urlRepository;

    UUID urlId;

    UUID countedUrlId;

    @BeforeEach
    void setUp() {
        Url url1 = new Url();
        url1.setLongUrl("https://google.com");
        url1.setShortUrl("abc123");
        urlRepository.save(url1);

        Url url2 = new Url();
        url2.setLongUrl("https://wp.pl");
        url2.setShortUrl("def456");
        urlRepository.save(url2);

        Url url3 = new Url();
        url3.setLongUrl("https://stormit.pl");
        url3.setShortUrl("ghi789");
        urlRepository.save(url3);

        Url url4 = new Url();
        url4.setLongUrl("https://yahoo.com");
        url4.setShortUrl("klo159");
        urlRepository.save(url4);

        UrlAnalytics analytics1 = new UrlAnalytics();
        analytics1.setUrl(url1);
        analytics1.setTotalClicks(6L);
        analytics1.setClickDate(Instant.now());
        urlAnalyticsRepository.save(analytics1);

        UrlAnalytics analytics2 = new UrlAnalytics();
        analytics2.setUrl(url2);
        analytics2.setTotalClicks(10L);
        analytics2.setClickDate(Instant.now());
        urlAnalyticsRepository.save(analytics2);

        UrlAnalytics analytics3 = new UrlAnalytics();
        analytics3.setUrl(url3);
        analytics3.setTotalClicks(150L);
        analytics3.setClickDate(Instant.now());
        urlAnalyticsRepository.save(analytics3);

        UrlAnalytics analytics4 = new UrlAnalytics();
        analytics4.setUrl(url4);
        analytics4.setTotalClicks(50L);
        analytics4.setClickDate(Instant.now());
        urlAnalyticsRepository.save(analytics4);

        countedUrlId = urlRepository.findUrlByLongUrl("https://google.com").get().getId();
        urlId = urlAnalyticsRepository.findAll().get(0).getUrl().getId();
    }

    @Test
    void shouldReturnAllAnalytics() {
        // given

        // when
        List<UrlAnalyticsDto> allAnalyticsDto = urlAnalyticsService.getAllAnalytics();

        // then
        assertEquals(4, allAnalyticsDto.size());
    }

    @Test
    void shouldThrowExceptionWhenAnalyticsWithUrlIdDoesNotExist() {
        // given
        urlAnalyticsRepository.deleteAll();

        // when

        // then
        assertThrows(ApiException.class, () -> urlAnalyticsService.getAnalyticsByUrlId(urlId));
        Assertions.assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> urlAnalyticsService.getAnalyticsByUrlId(urlId))
                .withMessageContaining("does not exist!");
    }

    @Test
    void shouldCountedAmountOfClicksForShortUrl() {
        // given
        Url url = urlRepository.findUrlByLongUrl("https://google.com").orElseThrow();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("95.160.156.244");

        // when
        urlAnalyticsService.setAnalyticsData(url, servletRequest);
        Long totalClicksOnUrl = urlAnalyticsRepository.findMaxClickOnShortUrlByUrlId(urlId);

        // then
        assertEquals(7L, totalClicksOnUrl);
    }

    @Test
    void shouldReturnLocalizationValueWhenIpAddressFoundCorrectly() {
        // given
        String ipAddress = "95.160.156.244";

        // when
        UrlAnalyticsLocalizationDto response = ipLocalizationService.getIpLocalization(ipAddress);

        // then
        assertEquals(response.getCityLocalization(), "Warsaw");
        assertEquals(response.getCountryLocalization(), "Poland");
        assertEquals(response.getIsoCode(), "PL");
    }

    @Test
    void shouldReturnNullWhenAddressIpIsInvalid() {
        // given
        String ipAddress = "Invalid";

        // when

        // then
        assertThrows(RuntimeException.class, () -> ipLocalizationService.getIpLocalization(ipAddress));
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> ipLocalizationService.getIpLocalization(ipAddress))
                .withMessageContaining("Failed to get location");
    }

    @Test
    void shouldReturnMostPopularUrlsWhenUrlsExist() {
        // given

        // when
        List<UrlDto> result = urlAnalyticsService.findMostPopularUrls();

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(150L, result.get(0).getAnalytics().getTotalClicks());
        assertEquals(50L, result.get(1).getAnalytics().getTotalClicks());
        assertEquals(10L, result.get(2).getAnalytics().getTotalClicks());
    }

    @Test
    void shouldReturnEmptyListWhenUrlsDoesntExist() {
        // given
        urlAnalyticsRepository.deleteAll();

        // when
        List<UrlDto> result = urlAnalyticsService.findMostPopularUrls();

        // then
        assertTrue(result.isEmpty());
    }
}
