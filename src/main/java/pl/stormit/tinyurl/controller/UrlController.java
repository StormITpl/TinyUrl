package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/urls")
@RequiredArgsConstructor
@Validated
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlDto> generateShortUrl(@Valid @RequestBody UrlDto urlDto) {
        urlService.generateShortUrl(urlDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message","You have successfully completed the generation of shortUrl");
        return new ResponseEntity<>(urlDto, headers, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<UrlDto> createShortUrl(@Valid @RequestBody UrlDto urlDto) {
        urlService.createShortUrl(urlDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message","You have successfully completed the creation of shortUrl for your longUrl");
        return new ResponseEntity<>(urlDto, headers, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Url> getUrls() {
        return urlService.getUrls();
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> longUrlRedirect(@PathVariable String shortUrl) throws URISyntaxException {
        urlService.shortUrlDoesNotExist(shortUrl);
        URI uri = new URI(urlService.startsWithHttpOrHttpsProtocolLongUrl(shortUrl));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }


}
