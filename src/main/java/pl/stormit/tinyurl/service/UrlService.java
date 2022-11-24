package pl.stormit.tinyurl.service;


import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;
import pl.stormit.tinyurl.exception.ApiException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UrlService {

    @Autowired
    private final UrlRepository urlRepository;

    public Url generateShortUrl(UrlDto urlDto) {

        if (!urlDto.getLongUrl().isBlank()) {

            Url urlToSave = new Url();
            String encodedUrl = encodeUrl(urlDto.getLongUrl());
            urlToSave.setCreationDate(LocalDate.now());
            urlToSave.setLongUrl(urlDto.getLongUrl());
            urlToSave.setShortUrl(encodedUrl);
            Url urlToReturn = saveShortUrl(urlToSave);

            return urlToReturn;
        }
        throw new ApiException("Change the request your longUrl is empty!");
    }

    private String encodeUrl(String longUrl) {
        LocalDateTime time = LocalDateTime.now();
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(longUrl.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedUrl;
    }

    public Url saveShortUrl(Url url) {
        Url urlToReturn = urlRepository.save(url);
        return urlToReturn;
    }

    public List<Url> getUrls() {
        return urlRepository.findAll();
    }

    public Optional<Url> getByShortUrl(String shortUrl) throws ApiException {
            return urlRepository.findUrlByShortUrl(shortUrl);
    }

    public String startsWithHttpOrHttpsProtocolLongUrl(String shortUrl) throws NoSuchElementException {
        Optional<Url> urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl);

        if (urlByShortUrl.get().getLongUrl().contains("https://") ||
                urlByShortUrl.get().getLongUrl().contains("http://")) {
            return urlByShortUrl.get().getLongUrl();
        } else {
            return "https://" + urlByShortUrl.get().getLongUrl();
        }
    }

    public boolean shortUrlExist(String shortUrl) {
        Optional<Url> urlByShortUrl = urlRepository.findUrlByShortUrl(shortUrl);

        if(urlByShortUrl.isPresent()){
            return true;
        } else {
            throw new ApiException("The short url: " + shortUrl + ", does not exist.");
        }
    }
}
