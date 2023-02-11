package pl.stormit.tinyurl.service;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.domain.repository.UrlExpiryRepository;

import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UrlExpiryService {

    private final UrlExpiryRepository urlExpiryRepository;

    private final static int TWO_WEEKS = 1209600;


    public UrlExpiry createUrlExpiryDate(Url url) {

        UrlExpiry urlExpiry = new UrlExpiry();

        isAccountPremium(urlExpiry);

        urlExpiry.setUrl(url);

        return urlExpiryRepository.save(urlExpiry);
    }

    public UrlExpiry isAccountPremium(UrlExpiry urlExpiry){

        if(!urlExpiry.getIsPremium()){
            urlExpiry.setExpirationDate(Instant.now().plusSeconds(20)); //TWO_WEEKS
        }

        return urlExpiry;
    }

    public List<UrlExpiry> getAllExpiries() {
        return urlExpiryRepository.findAll();
    }

    @Scheduled(fixedDelay = 15000)
    public void deleteExpiredDateUrls() {

        Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "Start searching expired urls");

        Instant checkInstant = Instant.now();

        List<UrlExpiry> expires = urlExpiryRepository.findAll().stream()
                .filter(urlExpiry -> urlExpiry.getExpirationDate().isBefore(checkInstant)).toList();

        if(expires.size()>0) {
            logger.log(Level.INFO, "Expiry Short Urls:");
            for (int i = 0; i < expires.size(); i++) {
                logger.log(Level.INFO, "ID: " + expires.get(i).getUrl().getId() + ", Long Url: " + expires.get(i).getUrl().getLongUrl() + ", Short Url: " + expires.get(i).getUrl().getShortUrl());
            }
        }
        urlExpiryRepository.deleteAll(expires);
    }
}
