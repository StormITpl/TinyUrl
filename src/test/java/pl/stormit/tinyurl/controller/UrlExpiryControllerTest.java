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
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.service.UrlExpiryService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UrlExpiryControllerTest {

    @MockBean
    private UrlExpiryService urlExpiryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllExpires() throws Exception {
        //given
        List<UrlExpiry> urlExpiryList = listOfUrlExpiry();
        given(urlExpiryService.getAllExpires()).willReturn(urlExpiryList);

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/expiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(urlExpiryList))));
        //then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));
    }

    private List<UrlExpiry> listOfUrlExpiry() {
        Url url1 = new Url("www.storimit.pl", "abc123");
        Url url2 = new Url("www.facebook.pl", "def456");
        Url url3 = new Url("www.google.pl", "ghi789");
        UrlExpiry urlExpiry1 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(500), false, url1);
        UrlExpiry urlExpiry2 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(20), true, url2);
        UrlExpiry urlExpiry3 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().minusSeconds(20), false, url3);

        return List.of(urlExpiry1, urlExpiry2, urlExpiry3);
    }
}