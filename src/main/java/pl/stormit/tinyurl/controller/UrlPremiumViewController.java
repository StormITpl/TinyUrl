package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlAnalyticsService;
import pl.stormit.tinyurl.service.UrlService;

import java.util.UUID;

@Controller
@RequestMapping(path = "/url-premium")
@RequiredArgsConstructor
public class UrlPremiumViewController {

    public static final String INDEX_PREMIUM = "indexPremium";
    private final UrlService urlService;

    @GetMapping
    public String showPremiumForm(Model model) {
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);
        model.addAttribute("allUrls", urlService.getUrls());

        return INDEX_PREMIUM;
    }

    @PostMapping("/generate")
    public String generateShortUrl(Model model, UrlDto urlDto) {
        UrlDto generatedUrlDto = urlService.generateShortUrl(urlDto);
        urlDto.setShortUrl(generatedUrlDto.getShortUrl());
        model.addAttribute("allUrls", urlService.getUrls());
        model.addAttribute("activeForm", "form1");

        return INDEX_PREMIUM;
    }

    @PostMapping("/create")
    public String createShortUrl(Model model, UrlDto urlDto) {
        if (!urlService.isLongUrlExists(urlDto.getLongUrl())) {
            model.addAttribute("errorLongUrl", "Long URL already exists.");
            return INDEX_PREMIUM;
        }

        if (!urlService.isShortUrlExists(urlDto.getShortUrl())) {
            model.addAttribute("errorShortUrl", "Short URL already exists.");
            return INDEX_PREMIUM;
        }

        UrlDto createdUrlDto = urlService.createShortUrl(urlDto);
        urlDto.setShortUrl(createdUrlDto.getShortUrl());
        model.addAttribute("allUrls", urlService.getUrls());
        model.addAttribute("activeForm", "form2");

        return INDEX_PREMIUM;
    }

    @PostMapping("/delete/{id}")
    public String deleteUrl(@PathVariable UUID id) {
        urlService.deleteUrl(id);
        return "redirect:/url-premium";
    }
}
