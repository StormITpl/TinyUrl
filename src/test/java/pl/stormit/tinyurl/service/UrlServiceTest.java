package pl.stormit.tinyurl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ApiException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UrlServiceTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlService urlService;

    @Test
    void shouldCreateShortUrlCorrectly() {
        // given
        UrlDto urlDTO = new UrlDto("http://www.google.com", "");

        // when
        Url result = urlService.generateShortUrl(urlDTO);

        // then
        assertThat(result).isEqualTo(urlRepository.getById(result.getId()));
    }

    @Test
    void shouldThrowAnExceptionWhenLongUrlIsEmpty() throws Exception {
        //given
        UrlDto urlDto = new UrlDto("", "");

        //when

        //then
        assertThrows(ApiException.class, () -> urlService.generateShortUrl(urlDto));
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> urlService.generateShortUrl(urlDto)).withMessage("Change the request your longUrl is empty!");
    }

    @Test
    void shouldThrowAnExceptionWhenOnlyWhiteSpacesInLongUrl() throws Exception {
        //given
        UrlDto urlDto = new UrlDto("     ", "");

        //when

        //then
        assertThrows(ApiException.class, () -> urlService.generateShortUrl(urlDto));
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> urlService.generateShortUrl(urlDto)).withMessage("Change the request your longUrl is empty!");
    }

    @Test
    void shouldReturnLongUrlWithoutProtocolWithHttpsProtocol(){
        //given
        Url url = new Url("www.cnn.com", "kbn132");
        urlRepository.save(url);

        //when
        String result = urlService.startsWithHttpOrHttpsProtocolLongUrl(url.getShortUrl());

        //then
        String expected = "https://www.cnn.com";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnLongUrlWithProtocol(){
        //given
        Url url = new Url("https://www.cnn.com", "kbn132");
        urlRepository.save(url);

        //when
        String result = urlService.startsWithHttpOrHttpsProtocolLongUrl(url.getShortUrl());

        //then
        String expected = "https://www.cnn.com";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnUrlByShortUrlWhenLongUrlHasProtocol(){
        //given
        Url expected = new Url("https://www.cnn.com", "kbn132");
        urlRepository.save(expected);

        //when
        Optional<Url> result = urlRepository.findUrlByShortUrl("kbn132");

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnUrlByShortUrlWhenLongUrlHasNoProtocol(){
        //given
        Url expected = new Url("www.cnn.com", "kbn132");
        urlRepository.save(expected);

        //when
        Optional<Url> result = urlRepository.findUrlByShortUrl("kbn132");

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlHaveLowerCaseUpperCaseAndDigits(){
        //given
        String expected = "aaaAA11";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyLowerCase(){
        //given
        String expected = "aaaaa";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyUpperCase(){
        //given
        String expected = "AAAAAA";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldPassWhenShortUrlOnlyDigits(){
        //given
        String expected = "012345";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotPassWhenShortUrlIsTooSort(){
        //given
        String expected = "aaa";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNotPassWhenShortUrlIsTooLong(){
        //given
        String expected = "aaaaaaaaa";

        //when
        String result = urlService.isShortUrlCorrect(expected);

        //then
        assertThat(result).isEqualTo(expected);
    }
}