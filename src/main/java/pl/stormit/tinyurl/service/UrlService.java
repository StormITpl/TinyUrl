package pl.stormit.tinyurl.service;


import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.dto.UrlMapper;
import pl.stormit.tinyurl.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UrlService {

    @Autowired
    private final UrlRepository urlRepository;

    private final UrlExpiryService urlExpiryService;

    private final UrlAnalyticsService urlAnalyticsService;
    
    private final UrlMapper urlMapper;

    public UrlDto generateShortUrl(UrlDto urlDto) {
        longUrlExist(urlDto.getLongUrl());
        if (!urlDto.getLongUrl().isBlank()) {

            Url urlToSave = new Url();
            String encodedUrl = encodeUrl(urlDto.getLongUrl());
            urlToSave.setCreationDate(LocalDate.now());
            urlToSave.setLongUrl(urlDto.getLongUrl());
            urlToSave.setShortUrl(encodedUrl);
            Url savedUrl = urlRepository.save(urlToSave);
            urlExpiryService.createUrlExpiryDate(urlToSave);
            return urlMapper.mapUrlEntityToUrlDto(savedUrl);
        }
        throw new ApiException("Change the request your longUrl is empty!");
    }

    public UrlDto createShortUrl(UrlDto urlDto) {
        longUrlExist(urlDto.getLongUrl());
        Url urlToSave = new Url();
        urlToSave.setCreationDate(LocalDate.now());
        if (longUrlExist(urlDto.getLongUrl())) {
            urlToSave.setLongUrl(urlDto.getLongUrl());
        }
        if (shortUrlExist(isShortUrlCorrect(urlDto.getShortUrl()))) {
            urlToSave.setShortUrl(urlDto.getShortUrl());
        }
        Url savedUrl = urlRepository.save(urlToSave);
        urlExpiryService.createUrlExpiryDate(urlToSave);
        return urlMapper.mapUrlEntityToUrlDto(savedUrl);
    }

    private String encodeUrl(String longUrl) {
        LocalDateTime time = LocalDateTime.now();
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(longUrl.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedUrl;
    }

    public List<Url> getUrls() {
        return urlRepository.findAll();
    }

    public Optional<Url> getByShortUrl(String shortUrl) throws ApiException {
        return urlRepository.findUrlByShortUrl(shortUrl);
    }

    public String startsWithHttpOrHttpsProtocolLongUrl(String shortUrl, HttpServletRequest servletRequest) {
        Url urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl)
                .orElseThrow(() -> {
                    throw new ApiException("The short url: " + shortUrl + ", does not exist.");
                });
        urlAnalyticsService.setAnalyticsData(urlByShortUrl, servletRequest);

        if (urlByShortUrl.getLongUrl().contains("https://") ||
                urlByShortUrl.getLongUrl().contains("http://")) {
            return urlByShortUrl.getLongUrl();
        } else {
            return "https://" + urlByShortUrl.getLongUrl();
        }
    }

    public boolean shortUrlDoesNotExist(String shortUrl) {
        Optional<Url> urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl);

        if (urlByShortUrl.isPresent()) {
            return true;
        } else {
            throw new ApiException("The short url: " + shortUrl + ", does not exist.");
        }
    }

    public boolean shortUrlExist(String shortUrl) {
        Optional<Url> urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl);

        if (urlByShortUrl.isPresent()) {
            throw new ApiException("The short url: " + shortUrl + ", exist.");
        } else {
            return true;
        }
    }

    public boolean longUrlExist(String longUrl) {
        Optional<Url> urlByLongUrl = urlRepository.findUrlByLongUrl(longUrl);

        if (urlByLongUrl.isPresent()) {
            throw new ApiException("The long url: " + longUrl + ", exist.");
        } else {
            return true;
        }
    }

    public String isShortUrlCorrect(String shortUrl) {

        String regex = "^\\w{4,8}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(shortUrl);
        if (m.matches()) {
            return shortUrl;
        }
        throw new ApiException("The short url: " + shortUrl + ", is incorrect.");
    }
}
