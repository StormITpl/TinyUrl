package pl.stormit.tinyurl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.service.UrlService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping
    public List<Url> getUrl(){
        return urlService.getUrl();
    }

}
