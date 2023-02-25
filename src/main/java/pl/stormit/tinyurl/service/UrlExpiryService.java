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
public class UrlExpiryService implements UrlExpiryInterface {

    private final UrlExpiryRepository urlExpiryRepository;

    public UrlExpiry createUrlExpiryDate(Url url) {

        UrlExpiry urlExpiry = new UrlExpiry();

        isAccountPremium(urlExpiry);

        urlExpiry.setUrl(url);

        return urlExpiryRepository.save(urlExpiry);
    }

    public UrlExpiry isAccountPremium(UrlExpiry urlExpiry) {

        if (!urlExpiry.getIsPremium()) {
            urlExpiry.setExpirationDate(Instant.now().plusSeconds(20)); //TWO_WEEKS
            return urlExpiry;
        } else {
            urlExpiry.setIsPremium(true);
            urlExpiry.setExpirationDate(null);
            return urlExpiry;
        }
    }

    public List<UrlExpiry> getAllExpires() {
        return urlExpiryRepository.findAll();
    } //OK

    public List<UrlExpiry> getAllExpiredUrls(){
        Instant checkInstant = Instant.now();
        return urlExpiryRepository.findAll().stream()
                .filter(urlExpiry -> urlExpiry.getExpirationDate().isBefore(checkInstant)).toList();
    }

    public void printExpiredUrlsToDelete(List<UrlExpiry> expires){
        Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "Start searching expired urls");

        if (expires.size() > 0) {
            logger.log(Level.INFO, "Expiry Short Urls:");
            for (UrlExpiry expire : expires) {
                logger.log(Level.INFO, "ID: " + expire.getUrl().getId() + ", Long Url: " + expire.getUrl().getLongUrl() + ", Short Url: " + expire.getUrl().getShortUrl());
            }
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void deleteExpiredDateUrls() {
        List<UrlExpiry> expires = getAllExpiredUrls();

        printExpiredUrlsToDelete(expires);

        urlExpiryRepository.deleteAll(expires);
    }
}


