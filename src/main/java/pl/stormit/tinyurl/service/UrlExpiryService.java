package pl.stormit.tinyurl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.domain.repository.UrlExpiryRepository;
import pl.stormit.tinyurl.dto.UrlExpiryDto;
import pl.stormit.tinyurl.dto.UrlExpiryMapper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UrlExpiryService implements UrlExpiryInterface {

    public static final int DELAY_TIME = 15000;
    private final UrlExpiryRepository urlExpiryRepository;

    private final UrlExpiryMapper urlExpiryMapper;

    public UrlExpiryDto createUrlExpiryDate(Url url) {

        UrlExpiry urlExpiry = new UrlExpiry();
        isAccountPremium(urlExpiry);
        urlExpiry.setUrl(url);
        return urlExpiryMapper.mapUrlExpiryEntityToUrlExpiryDto(urlExpiryRepository.save(urlExpiry));
    }

    @Override
    public boolean isAccountPremium(UrlExpiry urlExpiry) {

        if (urlExpiry.getIsPremium()) {
            urlExpiry.setIsPremium(true);
            urlExpiry.setExpirationDate(null);
            return true;
        } else {
            urlExpiry.setIsPremium(false);
            urlExpiry.setExpirationDate(Instant.now().plusSeconds(TWO_WEEKS));
            return false;
        }
    }

    public List<UrlExpiryDto> getAllExpires(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UrlExpiry> urlExpiryPage = urlExpiryRepository.findAll(pageable);

        return urlExpiryPage.stream()
                .map(urlExpiryMapper::mapUrlExpiryEntityToUrlExpiryDto)
                .toList();
    }

    public List<UrlExpiry> getAllExpiredUrls() {
        Instant checkInstant = Instant.now();
        return urlExpiryRepository.findAll().stream()
                .filter(urlExpiry -> Objects.nonNull(urlExpiry.getExpirationDate()) && urlExpiry.getExpirationDate().isBefore(checkInstant)).toList();
    }

    public void printExpiredUrlsToDelete(List<UrlExpiry> expires) {
        Logger logger = Logger.getLogger(getClass().getName());

        logger.log(Level.INFO, "Start searching expired urls");

        if (expires.size() > 0) {
            logger.log(Level.INFO, "Expiry Short Urls:");
            for (UrlExpiry expire : expires) {
                logger.log(Level.INFO, "ID: " + expire.getUrl().getId() + ", Long Url: " + expire.getUrl().getLongUrl() + ", Short Url: " + expire.getUrl().getShortUrl());
            }
        }
    }

    @Scheduled(fixedDelay = DELAY_TIME)
    public void deleteExpiredDateUrls() {
        List<UrlExpiry> expires = getAllExpiredUrls();
        printExpiredUrlsToDelete(expires);
        urlExpiryRepository.deleteAll(expires);
    }
}
