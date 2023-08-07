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
@RequestMapping(path = "api/v1/url-premium")
@RequiredArgsConstructor
public class UrlPremiumViewController {

    private final UrlService urlService;

    @GetMapping
    public String showPremiumForm(Model model) {
        UrlDto urlDto = new UrlDto();
        model.addAttribute("urlDto", urlDto);
        model.addAttribute("allUrls", urlService.getUrls());

        return "indexPremium";
    }

    @PostMapping("generate")
    public String generateShortUrl(Model model, UrlDto urlDto) {
        UrlDto generatedUrlDto = urlService.generateShortUrl(urlDto);
        urlDto.setShortUrl(generatedUrlDto.getShortUrl());
        model.addAttribute("allUrls", urlService.getUrls());
        model.addAttribute("activeForm", "form1");

        return "indexPremium";
    }

    @PostMapping("create")
    public String createShortUrl(Model model, UrlDto urlDto) {
        UrlDto createdUrlDto = urlService.createShortUrl(urlDto);
        urlDto.setShortUrl(createdUrlDto.getShortUrl());
        model.addAttribute("allUrls", urlService.getUrls());
        model.addAttribute("activeForm", "form2");

        return "indexPremium";
    }

    @PostMapping("/delete/{id}")
    public String deleteUrl(@PathVariable UUID id) {
        urlService.deleteUrl(id);
        return "redirect:/api/v1/url-premium";
    }
}
