package pl.stormit.tinyurl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @MockBean
    private UrlService urlService;

    @MockBean
    private UrlController urlController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UrlDto urlDto;

    private URI uri;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void shouldReturnStatusCreatedWhenCreateShortUrlCorrectly() throws Exception {
        //given
        urlDto = new UrlDto("https://www.google.pl/", "");
        when(urlService.generateShortUrl(urlDto))
                .thenReturn(new Url("https://www.google.pl/", ""));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsNull() throws Exception {
        //given
        urlDto = new UrlDto(null, "");
        when(urlService.generateShortUrl(any()))
                .thenReturn(new Url(null, ""));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsEmpty() throws Exception {
        //given
        urlDto = new UrlDto("", "");
        when(urlService.generateShortUrl(any()))
                .thenReturn(new Url("", ""));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenOnlyWhiteSpacesInLongUrl() throws Exception {
        //given
        urlDto = new UrlDto("     ", "");
        when(urlService.generateShortUrl(any()))
                .thenReturn(new Url("     ", ""));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlDto))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldRedirectWhenUrlIsWithoutHttpsOrHttpProtocol() throws Exception {
        //given
        Url url = new Url("www.cnn.com", "kbn132");
        urlRepository.save(url);
        when(urlService.startsWithHttpsOrHttpsProtocolLongUrl(url.getShortUrl())
                .equals("https://www.cnn.com"));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/urls/kbn1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(url))));

        //then
        result.andExpect(status().is3xxRedirection());
    }

}