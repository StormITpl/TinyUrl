package pl.stormit.tinyurl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsMapper;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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

    @MockBean
    private UrlAnalyticsRepository urlAnalyticsRepository;

    @MockBean
    private UrlAnalyticsMapper urlAnalyticsMapper;

    @Test
    void shouldReturnAllAnalytics() {
        //given
        List<UrlAnalytics> urlAnalyticsList = createListOfAnalytics();

        //when
        when(urlAnalyticsRepository.findAll()).thenReturn(urlAnalyticsList);
        List<UrlAnalytics> allAnalytics = urlAnalyticsService.getAllAnalytics();

        //then
        assertThat(allAnalytics, hasSize(urlAnalyticsList.size()));
    }

    @Test
    void shouldThrowExceptionWhenAnalyticsWithUrlIdDoesNotExist() {
        //given

        //when
        given(urlAnalyticsRepository.findAllByUrlId(Mockito.any())).willReturn(null);

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
        when(urlAnalyticsRepository.findAllByUrlId(URL_ID_1)).thenReturn(urlAnalyticsList);
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
        UrlAnalyticsLocalizationDto response = urlAnalyticsService.getIpLocalization(ipAddress);

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
        UrlAnalyticsLocalizationDto response = urlAnalyticsService.getIpLocalization(ipAddress);

        //then
        assertNull(response.getCityLocalization());
        assertNull(response.getCountryLocalization());
        assertNull(response.getIsoCode());
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