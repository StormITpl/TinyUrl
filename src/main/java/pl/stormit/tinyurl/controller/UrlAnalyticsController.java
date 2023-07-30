package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.service.UrlAnalyticsService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "api/v1/analytics")
@RequiredArgsConstructor
public class UrlAnalyticsController {

    private final UrlAnalyticsService urlAnalyticsService;

    @GetMapping("/{url-id}")
    public ResponseEntity<List<UrlAnalyticsDto>> getAnalyticsByUrlId(@NotNull @PathVariable("url-id") UUID urlId) {

        List<UrlAnalyticsDto> urlAnalyticsByUrlIdList = urlAnalyticsService.getAnalyticsByUrlId(urlId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The list of analytics by url id has been successfully found");

        return new ResponseEntity<>(urlAnalyticsByUrlIdList, headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UrlAnalyticsDto>> getAllAnalytics() {

        List<UrlAnalyticsDto> urlAnalyticsDtoList = urlAnalyticsService.getAllAnalytics();
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The list of analytics has been successfully found");

        return new ResponseEntity<>(urlAnalyticsDtoList, headers, HttpStatus.OK);
    }

    @GetMapping("/most-popular")
    public ResponseEntity<List<UrlDto>> getMostPopularUrls() {

        List<UrlDto> mostPopularUrlsList = urlAnalyticsService.findMostPopularUrls();
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The most popular URL's has been successfully found");

        return new ResponseEntity<>(mostPopularUrlsList, headers, HttpStatus.OK);
    }
}
