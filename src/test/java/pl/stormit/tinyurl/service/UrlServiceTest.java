package pl.stormit.tinyurl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ApiException;

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
        Url result = urlRepository.findUrlByShortUrl("kbn132");

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnUrlByShortUrlWhenLongUrlHasNoProtocol(){
        //given
        Url expected = new Url("www.cnn.com", "kbn132");
        urlRepository.save(expected);

        //when
        Url result = urlRepository.findUrlByShortUrl("kbn132");

        //then
        assertThat(result).isEqualTo(expected);
    }
}