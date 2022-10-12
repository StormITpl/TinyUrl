package pl.stormit.tinyurl.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;
import pl.stormit.tinyurl.dto.UrlDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlService {

    @Autowired
    private final UrlRepository urlRepository;

    public Url generateShortUrl(UrlDto urlDto) {

        if (!urlDto.getLongUrl().isEmpty()){

            Url urlToSave = new Url();
            String encodedUrl = encodeUrl(urlDto.getLongUrl());
            urlToSave.setCreationDate(LocalDateTime.now());
            urlToSave.setLongUrl(urlDto.getLongUrl());
            urlToSave.setShortUrl(encodedUrl);
            Url urlToReturn = saveShortUrl(urlToSave);

            return urlToReturn;
        }
        throw new RuntimeException("Change the request your longUrl is empty!");
    }


    public List<Url> getUrl(){
        return urlRepository.findAll();
    }
}
