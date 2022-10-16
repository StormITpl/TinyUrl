package pl.stormit.tinyurl.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    @Autowired
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlDto> createShortUrl(@RequestBody UrlDto urlDto) {
        urlService.generateShortUrl(urlDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message","You have successfully completed the creation of shortUrl");
        return new ResponseEntity<>(urlDto,headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Url> getUrl() {
        return urlService.getUrl();
    }
}
