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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "api/v1/url-view")
@RequiredArgsConstructor
public class UrlViewController {

    private final UrlService urlService;

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping
    public String showForm(Model model) {
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", new UrlDto());
        return "index/index";
    }

    @GetMapping("add")
    public String addView(Model model){
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);


        return "index/index";
    }

    @PostMapping
    public String createShortUrl(UrlDto urlDto){
        UrlDto generatedUrlDto = urlService.generateShortUrl(urlDto);
        urlDto.setShortUrl(generatedUrlDto.getShortUrl());

        return "index/index";
    }

    @GetMapping({"most-popular"})
    public String getMostPopularUrls(Model model) {
        List<UrlDto> mostPopularUrls = urlAnalyticsService.findMostPopularUrls();
        model.addAttribute("mostPopularUrls", mostPopularUrls);

        return "index/index";
    }
}
