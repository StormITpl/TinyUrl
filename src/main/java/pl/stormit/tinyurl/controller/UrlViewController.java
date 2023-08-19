package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlAnalyticsService;
import pl.stormit.tinyurl.service.UrlService;

@Controller
@RequestMapping(path = "api/v1/url-view")
@RequiredArgsConstructor
public class UrlViewController {

    public static final String INDEX = "index";
    private final UrlService urlService;

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("urlDto", new UrlDto());
        model.addAttribute("mostPopularUrls", urlAnalyticsService.findMostPopularUrls());
        return INDEX;
    }

    @GetMapping("add")
    public String addView(Model model) {
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);

        return INDEX;
    }

    @PostMapping
    public String generateShortUrl(Model model, UrlDto urlDto) {
        UrlDto generatedUrlDto = urlService.generateShortUrl(urlDto);
        urlDto.setShortUrl(generatedUrlDto.getShortUrl());
        model.addAttribute("mostPopularUrls", urlAnalyticsService.findMostPopularUrls());

        return INDEX;
    }
}
