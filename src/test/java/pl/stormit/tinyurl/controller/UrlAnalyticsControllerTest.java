package pl.stormit.tinyurl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ResourceNotFoundException;
import pl.stormit.tinyurl.service.UrlAnalyticsService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlAnalyticsControllerTest {

    private static final UUID ID_1 = UUID.fromString("5d1b4c2c-9f1c-11ed-a8fc-0242ac120002");
    private static final UUID ID_2 = UUID.fromString("65a46dd8-9f1c-11ed-a8fc-0242ac120002");
    private static final UUID ID_3 = UUID.fromString("6cb92122-9f1c-11ed-a8fc-0242ac120002");
    private static final Long AMOUNT_OF_CLICKS_1 = 1L;
    private static final Long AMOUNT_OF_CLICKS_2 = 2L;
    private static final Long AMOUNT_OF_CLICKS_3 = 3L;

    @MockBean
    private UrlAnalyticsService urlAnalyticsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusOkWhenGetAllAnalyticsCorrectly() throws Exception {
        // given
        List<UrlAnalyticsDto> urlAnalyticsListDto = createListOfAnalyticsDto();
        given(urlAnalyticsService.getAllAnalytics()).willReturn(urlAnalyticsListDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/analytics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlAnalyticsListDto))));
        // then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));
    }

    @Test
    void shouldResponseStatusOkAndReturnEmptyList() throws Exception {
        // given

        // when
        MockHttpServletResponse result = mockMvc.perform(get("/api/v1/analytics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString()).isEqualTo(Collections.emptyList().toString());
    }

    @Test
    void shouldReturnStatusOkWhenGetAnalyticsByUrlIdIsCorrectly() throws Exception {
        // given
        Url url = new Url("www.google.pl", "817a3ec2");
        List<UrlAnalyticsDto> urlAnalyticsListDto = createListOfAnalyticsDto();
        when(urlAnalyticsService.getAnalyticsByUrlId(url.getId())).thenReturn(urlAnalyticsListDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/{id}", url.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlAnalyticsListDto))));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusNotFoundWhenUrlIdDoesNotExist() throws Exception {
        // given
        List<UrlAnalyticsDto> urlAnalyticsListDto = createListOfAnalyticsDto();
        when(urlAnalyticsService.getAnalyticsByUrlId(any())).thenThrow(ResourceNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlAnalyticsListDto))));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatusOkWhenFindMostPopularUrlsCorrectly() throws Exception {
        // given
        List<UrlDto> popularUrls = createListOfPopularUrls();
        when(urlAnalyticsService.findMostPopularUrls()).thenReturn(popularUrls);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/most-popular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(popularUrls))));
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(popularUrls.size()));
    }

    @Test
    void shouldReturnStatusNotFoundWhenMostPopularUrlsDoesNotExist() throws Exception {
        // given
        List<UrlAnalyticsDto> urlAnalyticsListDto = createListOfAnalyticsDto();
        when(urlAnalyticsService.findMostPopularUrls()).thenThrow(ResourceNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/most-popular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlAnalyticsListDto))));

        // then
        result.andExpect(status().isNotFound());
    }

    private List<UrlAnalyticsDto> createListOfAnalyticsDto() {
        UrlAnalyticsDto urlAnalytics1 = new UrlAnalyticsDto
                (ID_1, AMOUNT_OF_CLICKS_1, "Poland", "PL", "Warsaw", Instant.now());
        UrlAnalyticsDto urlAnalytics2 = new UrlAnalyticsDto
                (ID_2, AMOUNT_OF_CLICKS_2, "United States", "US", "Los Angeles", Instant.now());
        UrlAnalyticsDto urlAnalytics3 = new UrlAnalyticsDto
                (ID_3, AMOUNT_OF_CLICKS_3, "Poland", "PL", "Cracow", Instant.now());

        return List.of(urlAnalytics1, urlAnalytics2, urlAnalytics3);
    }

    private List<UrlDto> createListOfPopularUrls() {

        List<UrlDto> popularUrls = new ArrayList<>();

        UrlDto urlDto1 = new UrlDto(null, "https://google.com", "abc123", null, null);
        UrlDto urlDto2 = new UrlDto(null, "https://wp.pl", "def456", null, null);
        UrlDto urlDto3 = new UrlDto(null, "https://stormit.pl", "ghi789", null, null);
        UrlDto urlDto4 = new UrlDto(null, "https://yahoo.com", "klo159", null, null);
        UrlDto urlDto5 = new UrlDto(null, "https://stooq.pl", "xyz547", null, null);

        popularUrls.add(urlDto1);
        popularUrls.add(urlDto2);
        popularUrls.add(urlDto3);
        popularUrls.add(urlDto4);
        popularUrls.add(urlDto5);

        return popularUrls;
    }
}