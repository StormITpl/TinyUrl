package pl.stormit.tinyurl.service;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    private final UrlExpiryService urlExpiryService;

    private final UrlAnalyticsService urlAnalyticsService;

    private final UrlMapper urlMapper;

    @Transactional
    public UrlDto generateShortUrl(UrlDto urlDto) {
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

    @Transactional
    public UrlDto createShortUrl(UrlDto urlDto) {
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

    @Transactional(readOnly = true)
    public List<UrlDto> getUrls() {
        return urlRepository.findAll().stream()
                .map(urlMapper::mapUrlEntityToUrlDto)
                .toList();
    }

    private String encodeUrl(String longUrl) {
        LocalDateTime time = LocalDateTime.now();
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(longUrl.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedUrl;
    }

    public String getLongUrlByShortUrl(String shortUrl, HttpServletRequest servletRequest) {
        Url urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl)
                .orElseThrow(() -> new ApiException("The short url: " + shortUrl + ", does not exist."));

        urlAnalyticsService.setAnalyticsData(urlByShortUrl, servletRequest);

        if (urlByShortUrl.getLongUrl().startsWith("https://") || urlByShortUrl.getLongUrl().startsWith("http://")) {
            return urlByShortUrl.getLongUrl();
        } else {
            return "https://" + urlByShortUrl.getLongUrl();
        }
    }

    public boolean shortUrlExist(String shortUrl) {
        if (urlRepository.findUrlByShortUrl(shortUrl).isPresent()) {
            throw new ApiException("The short url: " + shortUrl + ", exists.");
        } else {
            return true;
        }
    }

    public boolean longUrlExist(String longUrl) {
        if (urlRepository.findUrlByLongUrl(longUrl).isPresent()) {
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
