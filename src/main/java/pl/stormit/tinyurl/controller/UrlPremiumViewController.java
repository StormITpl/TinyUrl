package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlAnalyticsService;
import pl.stormit.tinyurl.service.UrlService;

import java.util.UUID;

@Controller
@RequestMapping(path = "api/v1/url-premium")
@RequiredArgsConstructor
public class UrlPremiumViewController {

    private final UrlService urlService;

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping
    public String showPremiumForm(Model model) {
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);
        model.addAttribute("allUrls", urlService.getUrls());
        return "index/indexPremium";
    }

    @GetMapping("add")
    public String addView(Model model){
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);


        return "index/indexPremium";
    }

    @PostMapping
    public String generateShortUrl(UrlDto urlDto){
        urlService.generateShortUrl(urlDto);

        return "index/indexPremium";
    }

    @PostMapping("create")
    public String createShortUrl(UrlDto urlDto){
        urlService.createShortUrl(urlDto);

        return "index/indexPremium";
    }

    @PostMapping("/delete/{id}")
    public String deleteUrl(@PathVariable UUID id) {
        urlService.deleteUrl(id);
        return "redirect:/api/v1/url-premium";
    }
}
