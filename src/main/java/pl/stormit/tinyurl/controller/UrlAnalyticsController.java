package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.service.UrlAnalyticsService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/analytics")
@RequiredArgsConstructor
public class UrlAnalyticsController {

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping("/{url-id}")
    public ResponseEntity<List<UrlAnalytics>> getAnalyticsByUrlId(@NotNull @PathVariable("url-id") UUID urlId) {
        List<UrlAnalytics> urlAnalyticsByUrlIdList = urlAnalyticsService.getAnalyticsByUrlId(urlId);
        return new ResponseEntity<>(urlAnalyticsByUrlIdList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UrlAnalytics>> getAllAnalytics() {
        List<UrlAnalytics> urlAnalyticsDtoList = urlAnalyticsService.getAllAnalytics();
        return new ResponseEntity<>(urlAnalyticsDtoList, HttpStatus.OK);
    }
}
