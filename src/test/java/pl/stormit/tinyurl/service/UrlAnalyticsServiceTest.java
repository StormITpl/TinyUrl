package pl.stormit.tinyurl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsMapper;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ActiveProfiles({"test"})
@SpringBootTest
@Transactional
class UrlAnalyticsServiceTest {

    private static final UUID URL_ID_1 = UUID.fromString("13543cc8-9faf-11ed-a8fc-0242ac120002");
    private static final UUID ID_1 = UUID.fromString("5d1b4c2c-9f1c-11ed-a8fc-0242ac120002");
    private static final UUID ID_2 = UUID.fromString("65a46dd8-9f1c-11ed-a8fc-0242ac120002");
    private static final UUID ID_3 = UUID.fromString("6cb92122-9f1c-11ed-a8fc-0242ac120002");
    private static final UUID ID_4 = UUID.fromString("ea3433ea-9fa7-11ed-a8fc-0242ac120002");
    private static final Long AMOUNT_OF_CLICKS_1 = 1L;
    private static final Long AMOUNT_OF_CLICKS_2 = 2L;
    private static final Long AMOUNT_OF_CLICKS_3 = 3L;
    private static final Long AMOUNT_OF_CLICKS_4 = 4L;

    @Autowired
    private UrlAnalyticsService urlAnalyticsService;

    @Autowired
    private IpLocalizationService ipLocalizationService;

    @Autowired
    private UrlAnalyticsRepository realUrlAnalyticsRepository;

    @MockBean
    private UrlAnalyticsRepository mockUrlAnalyticsRepository;

    @Autowired
    private UrlRepository urlRepository;

    @MockBean
    private UrlAnalyticsMapper urlAnalyticsMapper;

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
        analytics1.setTotalClicks(10L);
        analytics1.setClickDate(Instant.now());
        realUrlAnalyticsRepository.save(analytics1);

        UrlAnalytics analytics2 = new UrlAnalytics();
        analytics2.setUrl(url2);
        analytics2.setTotalClicks(5L);
        analytics2.setClickDate(Instant.now());
        realUrlAnalyticsRepository.save(analytics2);

        UrlAnalytics analytics3 = new UrlAnalytics();
        analytics3.setUrl(url3);
        analytics3.setTotalClicks(150L);
        analytics3.setClickDate(Instant.now());
        realUrlAnalyticsRepository.save(analytics3);

        UrlAnalytics analytics4 = new UrlAnalytics();
        analytics4.setUrl(url4);
        analytics4.setTotalClicks(50L);
        analytics4.setClickDate(Instant.now());
        realUrlAnalyticsRepository.save(analytics4);
    }

    @Test
    void shouldReturnAllAnalytics() {
        //given
        List<UrlAnalytics> urlAnalyticsList = createListOfAnalytics();

        //when
        when(mockUrlAnalyticsRepository.findAll()).thenReturn(urlAnalyticsList);
        List<UrlAnalytics> allAnalytics = urlAnalyticsService.getAllAnalytics();

        //then
        assertThat(allAnalytics, hasSize(urlAnalyticsList.size()));
    }

    @Test
    void shouldThrowExceptionWhenAnalyticsWithUrlIdDoesNotExist() {
        //given

        //when
        given(mockUrlAnalyticsRepository.findAllByUrlId(any())).willReturn(null);

        //then
        assertThrows(ApiException.class, () -> urlAnalyticsService.getAnalyticsByUrlId(ID_1));
        Assertions.assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> urlAnalyticsService.getAnalyticsByUrlId(ID_1))
                .withMessageContaining("does not exist!");
    }

    @Test
    void shouldCountedAmountOfClicksForShortUrl() {
        //given
        Url url = new Url(URL_ID_1, "www.google.pl", "817a3ec2", null, null);
        List<UrlAnalytics> urlAnalyticsList = createListOfAnalytics();
        UrlAnalytics urlAnalytics4 = new UrlAnalytics
                (ID_4, AMOUNT_OF_CLICKS_4, "Poland", "PL", "Gdansk", Instant.now(), url);
        UrlAnalyticsDto urlAnalyticsDto = new UrlAnalyticsDto
                (ID_4, AMOUNT_OF_CLICKS_4, "Poland", "PL", "Gdansk", Instant.now());
        when(mockUrlAnalyticsRepository.findAllByUrlId(URL_ID_1)).thenReturn(urlAnalyticsList);
        when(urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalytics4)).thenReturn(urlAnalyticsDto);
        List<UrlAnalytics> newList = new ArrayList<>();

        //when
        newList.add(0, urlAnalyticsList.get(0));
        newList.add(1, urlAnalyticsList.get(1));
        newList.add(2, urlAnalyticsList.get(2));
        newList.add(3, urlAnalytics4);

        //then
        assertEquals(urlAnalytics4.getTotalClicks(), newList.get(3).getTotalClicks());
    }

    @Test
    void shouldReturnLocalizationValueWhenIpAddressFoundCorrectly() {
        //given
        String ipAddress = "95.160.156.244";

        //when
        UrlAnalyticsLocalizationDto response = ipLocalizationService.getIpLocalization(ipAddress);

        //then
        assertEquals(response.getCityLocalization(), "Warsaw");
        assertEquals(response.getCountryLocalization(), "Poland");
        assertEquals(response.getIsoCode(), "PL");
    }

    @Test
    void shouldReturnNullWhenAddressIpIsInvalid() {
        //given
        String ipAddress = "Invalid";

        //when

        //then
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
    }

    @Test
    void shouldReturnEmptyListWhenUrlsDoesntExist() {
        // given

        // when
        List<UrlDto> result = urlAnalyticsService.findMostPopularUrls();

        // then
        assertTrue(result.isEmpty());
    }

    private List<UrlAnalytics> createListOfAnalytics() {
        Url url = new Url(URL_ID_1, "www.google.pl", "817a3ec2", null, null);
        UrlAnalytics urlAnalytics1 = new UrlAnalytics
                (ID_1, AMOUNT_OF_CLICKS_1, "Poland", "PL", "Warsaw", Instant.now(), url);
        UrlAnalytics urlAnalytics2 = new UrlAnalytics
                (ID_2, AMOUNT_OF_CLICKS_2, "United States", "US", "Los Angeles", Instant.now(), url);
        UrlAnalytics urlAnalytics3 = new UrlAnalytics
                (ID_3, AMOUNT_OF_CLICKS_3, "Poland", "PL", "Cracow", Instant.now(), url);

        return List.of(urlAnalytics1, urlAnalytics2, urlAnalytics3);
    }
}