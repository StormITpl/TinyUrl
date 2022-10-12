package pl.stormit.tinyurl.service;


import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlService {

    @Autowired
    private final UrlRepository urlRepository;

    public Url generateShortUrl(UrlDto urlDto) {

        if (!urlDto.getLongUrl().isEmpty()) {

            Url urlToSave = new Url();
            String encodedUrl = encodeUrl(urlDto.getLongUrl());
            urlToSave.setCreationDate(LocalDate.now());
            urlToSave.setLongUrl(urlDto.getLongUrl());
            urlToSave.setShortUrl(encodedUrl);
            Url urlToReturn = saveShortUrl(urlToSave);

            return urlToReturn;
        }
        throw new RuntimeException("Change the request your longUrl is empty!");
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

    public List<Url> getUrl() {
        return urlRepository.findAll();
    }
}
