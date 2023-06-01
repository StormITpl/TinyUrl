package pl.stormit.tinyurl.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.domain.repository.UrlExpiryRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UrlExpiryServiceTest {

    @MockBean
    private UrlExpiryRepository urlExpiryRepository;

    @Autowired
    private UrlExpiryService urlExpiryService;


    @Test
    void createUrlExpiryDate() {
        //given
        Url url = new Url("www.facebook.pl", "def456");
        UrlExpiry urlExpiry = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(20), true, url);

        //when
        urlExpiryRepository.save(urlExpiry);

        //then
        assertNotNull(urlExpiryRepository.findById(urlExpiry.getId()));

    }

    @Test
    void shouldSetExpirationDateOnNullWhenIsPremium() {
        //given
        Url url1 = new Url("www.facebook.pl", "def456");
        UrlExpiry urlExpiry1 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(20), true, url1);

        //when
        urlExpiryService.isAccountPremium(urlExpiry1);

        //then
        assertNull(urlExpiry1.getExpirationDate());
    }

    @Test
    void shouldSetExpirationDateWhenIsNotPremium() {
        //give
        Url url1 = new Url("www.facebook.pl", "def456");
        UrlExpiry urlExpiry1 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(20), false, url1);

        //when
        urlExpiryService.isAccountPremium(urlExpiry1);

        //then
        assertNotNull(urlExpiry1.getExpirationDate());
    }

    @Test
    void shouldGetAllExpires() {
        //given
        List<UrlExpiry> urlUrlExpiry = listOfUrlExpiry();
        urlExpiryRepository.saveAll(urlUrlExpiry);

        //when
        when(urlExpiryRepository.findAll()).thenReturn(urlUrlExpiry);

        //then
        assertEquals(3, urlUrlExpiry.size());
    }

    @Test
    void shouldGetAllExpiredDateUrls() {
        //given
        List<UrlExpiry> urlUrlExpiry = listOfUrlExpiry();
        given(urlExpiryRepository.findAll()).willReturn(urlUrlExpiry);
        //when
        List<UrlExpiry> result = urlExpiryService.getAllExpiredUrls();
        //then
        assertEquals(1, result.size());
    }

    private List<UrlExpiry> listOfUrlExpiry() {
        Url url1 = new Url("www.stormit.pl", "abc123");
        Url url2 = new Url("www.facebook.pl", "def456");
        Url url3 = new Url("www.google.pl", "ghi789");
        UrlExpiry urlExpiry1 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(500), false, url1);
        UrlExpiry urlExpiry2 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(20), true, url2);
        UrlExpiry urlExpiry3 = new UrlExpiry(UUID.randomUUID(), Instant.now(), Instant.now().minusSeconds(20), false, url3);

        return List.of(urlExpiry1, urlExpiry2, urlExpiry3);
    }
}