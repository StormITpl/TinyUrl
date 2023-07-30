package pl.stormit.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stormit.tinyurl.dto.UrlExpiryDto;
import pl.stormit.tinyurl.service.UrlExpiryService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/expires")
@RequiredArgsConstructor
public class UrlExpiryController {

    private final UrlExpiryService urlExpiryService;

    @GetMapping
    public ResponseEntity<List<UrlExpiryDto>> getAllExpires() {

        List<UrlExpiryDto> urlExpiryDtoList = urlExpiryService.getAllExpires();
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The list of expires has been successfully found");

        return new ResponseEntity<>(urlExpiryDtoList, headers, HttpStatus.OK);
    }
}
