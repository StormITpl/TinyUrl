package pl.stormit.tinyurl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlExpiryDto;
import pl.stormit.tinyurl.service.UrlExpiryService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlExpiryControllerTest {

    @MockBean
    private UrlExpiryService urlExpiryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllExpires() throws Exception {
        // given
        List<UrlExpiryDto> urlExpiryList = listOfUrlExpiry();
        given(urlExpiryService.getAllExpires(0,20)).willReturn(urlExpiryList);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/expires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlExpiryList))));
        // then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));
    }

    private List<UrlExpiryDto> listOfUrlExpiry() {
        Url url1 = new Url("www.stormit.pl", "abc123");
        Url url2 = new Url("www.facebook.pl", "def456");
        Url url3 = new Url("www.google.pl", "ghi789");

        UrlExpiryDto urlExpiryDto1 = new UrlExpiryDto();
        urlExpiryDto1.setShortUrl(url1.getShortUrl());
        urlExpiryDto1.setCreationDate(Instant.now());
        urlExpiryDto1.setExpirationDate(Instant.now().plusSeconds(500));
        urlExpiryDto1.setIsPremium(false);

        UrlExpiryDto urlExpiryDto2 = new UrlExpiryDto();
        urlExpiryDto2.setShortUrl(url2.getShortUrl());
        urlExpiryDto2.setCreationDate(Instant.now());
        urlExpiryDto2.setExpirationDate(Instant.now().plusSeconds(20));
        urlExpiryDto2.setIsPremium(true);

        UrlExpiryDto urlExpiryDto3 = new UrlExpiryDto();
        urlExpiryDto3.setShortUrl(url3.getShortUrl());
        urlExpiryDto3.setCreationDate(Instant.now());
        urlExpiryDto3.setExpirationDate(Instant.now().minusSeconds(20));
        urlExpiryDto3.setIsPremium(false);

        return List.of(urlExpiryDto1, urlExpiryDto2, urlExpiryDto3);
    }
}
