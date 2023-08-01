package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlService;

@Controller
@RequestMapping(path = "api/v1/urlview")
@RequiredArgsConstructor
public class UrlViewController {

    private final UrlService urlService;

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
        urlService.generateShortUrl(urlDto);

        return "index/index";
    }
}
