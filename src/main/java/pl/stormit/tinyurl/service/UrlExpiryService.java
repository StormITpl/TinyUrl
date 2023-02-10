package pl.stormit.tinyurl.service;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.domain.repository.UrlExpiryRepository;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlExpiryService {

    @Autowired
    private final UrlExpiryRepository urlExpiryRepository;

    public UrlExpiry createUrlExpiryDate(Url url) {

        UrlExpiry urlExpiry = new UrlExpiry();

        urlExpiry.setUrl(url);

        return urlExpiryRepository.save(urlExpiry);
    }

    @Scheduled(fixedDelay = 15000)
    public void deleteExpiredDateUrls() {

        System.out.println();
        System.out.println("Start searching expired urls");

        Instant checkInstant = Instant.now();

        List<UrlExpiry> expires = urlExpiryRepository.findAll().stream()
                .filter(urlExpiry -> urlExpiry.getExpirationDate().isBefore(checkInstant)).toList();


        System.out.println();

        for(int i = 0; i < expires.size(); i++){
            System.out.println(expires.get(i).getUrl().getId() + " " + expires.get(i).getUrl().getLongUrl() + " " + expires.get(i).getUrl().getShortUrl());
        }

        System.out.println();

        urlExpiryRepository.deleteAll(expires);
    }

}
