package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.service.UrlExpiryService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/expiry")
@RequiredArgsConstructor
public class UrlExpiryController {

    private final UrlExpiryService urlExpiryService;

    @GetMapping
    public ResponseEntity<List<UrlExpiry>> getAllExpiries() {
        List<UrlExpiry> urlExpiryList = urlExpiryService.getAllExpiries();
        return new ResponseEntity<>(urlExpiryList, HttpStatus.OK);
    }
}
