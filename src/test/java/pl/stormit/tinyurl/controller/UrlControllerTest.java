package pl.stormit.tinyurl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @MockBean
    private UrlService urlService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UrlDto urlDto;

    @Test
    void shouldReturnStatusCreatedWhenCreateShortUrlCorrectly() throws Exception {
        // given
        urlDto = new UrlDto("https://www.google.pl/", "", null);
        when(urlService.generateShortUrl(urlDto))
                .thenReturn(new UrlDto("https://www.google.pl/", "", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsNull() throws Exception {
        // given
        urlDto = new UrlDto(null, "", null);
        when(urlService.generateShortUrl(any()))
                .thenReturn(new UrlDto(null, "", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsEmpty() throws Exception {
        // given
        urlDto = new UrlDto("", "", null);
        when(urlService.generateShortUrl(any()))
                .thenReturn(new UrlDto("", "", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenOnlyWhiteSpacesInLongUrl() throws Exception {
        // given
        urlDto = new UrlDto("     ", "", null);
        when(urlService.generateShortUrl(any()))
                .thenReturn(new UrlDto("     ", "", null));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldRedirectToLongUrlWhenShortUrlIsCorrectly() throws Exception {
        // given
        urlDto = new UrlDto("www.cnn.com", "kbr345", null);
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        URI uri = new URI("www.cnn.com");
        when(urlService.getLongUrlByShortUrl(Mockito.eq("kbr345"), any(HttpServletRequest.class)))
                .thenReturn("www.cnn.com");

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/urls/kbr345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        // then
        result.andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("www.cnn.com"));
    }
}
