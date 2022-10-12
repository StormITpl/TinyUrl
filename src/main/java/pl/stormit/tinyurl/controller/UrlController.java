package pl.stormit.tinyurl.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/urls")
@AllArgsConstructor
public class UrlController {

    @Autowired
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<Object> createShortUrl(@RequestBody UrlDto urlDto) {
        return ResponseEntity.ok(urlService.generateShortUrl(urlDto));
    }

    @GetMapping
    public List<Url> getUrl() {
        return urlService.getUrl();
    }
}
