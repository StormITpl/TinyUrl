package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.service.UrlAnalyticsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/analytics")
@RequiredArgsConstructor
public class UrlAnalyticsController {

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping("/{url-id}")
    public List<UrlAnalytics> getAnalyticsByUrlId(@PathVariable("url-id") UUID urlId) {
        return urlAnalyticsService.getAnalyticsByUrlId(urlId);
    }

    @GetMapping
    public List<UrlAnalytics> getAllAnalytics() {
        return urlAnalyticsService.getAllAnalytics();
    }
}
