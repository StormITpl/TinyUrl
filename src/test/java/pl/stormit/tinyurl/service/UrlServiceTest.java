package pl.stormit.tinyurl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.dto.UrlMapper;
import pl.stormit.tinyurl.exception.ApiException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UrlServiceTest {

    public static final UUID ID_1 = UUID.fromString("0175650a-a076-11ed-a8fc-0242ac120002");

    @MockBean
    private UrlRepository urlRepository;

    @Autowired
    private UrlService urlService;

    @MockBean
    private UrlMapper urlMapper;

    @Test
    void shouldCreateShortUrlCorrectly() {
        // given
        Url url = new Url(ID_1, "https://www.google.com", "yr49u12", LocalDate.now(), null, null);
        UrlDto createdUrlDto = new UrlDto("https://www.google.com", "yr49u12", null);
        UrlDto urlDto = new UrlDto("https://www.google.com", "yr49u12", null);


        // when
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlMapper.mapUrlEntityToUrlDto(url)).thenReturn(urlDto);
        UrlDto returnUrl = urlService.generateShortUrl(createdUrlDto);

        // then
        assertEquals(url.getShortUrl(), returnUrl.getShortUrl());
        assertEquals(url.getLongUrl(), returnUrl.getLongUrl());
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsEmpty() {
        //given
        Url url = new Url(ID_1, "", "", LocalDate.now(), null, null);
        UrlDto createdUrlDto = new UrlDto("", "", null);
        UrlDto urlDto = new UrlDto("", "", null);

        //when
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlMapper.mapUrlEntityToUrlDto(url)).thenReturn(urlDto);

        //then
        assertThrows(ApiException.class, () -> urlService.generateShortUrl(createdUrlDto));
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> urlService.generateShortUrl(createdUrlDto)).withMessage("Change the request your longUrl is empty!");
    }

    @Test
    void shouldThrowAnExceptionWhenOnlyWhiteSpacesInLongUrl() {
        //given
        Url url = new Url(ID_1, "   ", "", LocalDate.now(), null, null);
        UrlDto createdUrlDto = new UrlDto("    ", "", null);
        UrlDto urlDto = new UrlDto("   ", "", null);

        //when
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlMapper.mapUrlEntityToUrlDto(url)).thenReturn(urlDto);

        //then
        assertThrows(ApiException.class, () -> urlService.generateShortUrl(createdUrlDto));
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> urlService.generateShortUrl(createdUrlDto)).withMessage("Change the request your longUrl is empty!");
    }

    @Test
    void shouldReturnLongUrlWithoutProtocolWithHttpsProtocol() {
        //given
        Url url = new Url("www.cnn.com", "kbn132");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        //when
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(url));
        String result = urlService.getLongUrlByShortUrl(url.getShortUrl(), servletRequest);

        //then
        String expected = "https://www.cnn.com";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnLongUrlWithProtocol() {
        //given
        Url url = new Url("https://www.cnn.com", "kbn132");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        //when
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(url));
        String result = urlService.getLongUrlByShortUrl(url.getShortUrl(), servletRequest);

        //then
        String expected = "https://www.cnn.com";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnUrlByShortUrlWhenLongUrlHasProtocol() {
        //given
        Url expected = new Url("https://www.cnn.com", "kbn132");

        //when
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(expected));

        //then
        assertThat(urlRepository.findUrlByShortUrl(expected.getShortUrl()).get()).isEqualTo(expected);
    }

    @Test
    void shouldReturnUrlByShortUrlWhenLongUrlHasNoProtocol() {
        //given
        Url expected = new Url("www.cnn.com", "kbn132");

        //when
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(expected));

        //then
        assertThat(urlRepository.findUrlByShortUrl(expected.getShortUrl()).get()).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlHaveLowerCaseUpperCaseAndDigits() {
        //given
        String expected = "aaaAA11";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyLowerCase() {
        //given
        String expected = "aaaaa";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyUpperCase() {
        //given
        String expected = "AAAAAA";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyDigits() {
        //given
        String expected = "012345";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotPassWhenShortUrlIsTooSort() {
        //given
        String shortUrl = "aaa";

        //when

        //then
        assertThrows(ApiException.class, () -> urlService.isShortUrlCorrect(shortUrl));
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> urlService.isShortUrlCorrect(shortUrl)).withMessage("The short url: " + shortUrl + ", is incorrect.");
    }

    @Test
    void shouldNotPassWhenShortUrlIsTooLong() {
        //given
        String shortUrl = "aaaaaaaaa";

        //when

        //then
        assertThrows(ApiException.class, () -> urlService.isShortUrlCorrect(shortUrl));
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> urlService.isShortUrlCorrect(shortUrl)).withMessage("The short url: " + shortUrl + ", is incorrect.");
    }

    @Test
    void shouldNotPassWhenShortUrlHasWhiteSpaces() {
        //given
        String shortUrl = "aaaa a";

        //when

        //then
        assertThrows(ApiException.class, () -> urlService.isShortUrlCorrect(shortUrl));
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> urlService.isShortUrlCorrect(shortUrl)).withMessage("The short url: " + shortUrl + ", is incorrect.");
    }
}