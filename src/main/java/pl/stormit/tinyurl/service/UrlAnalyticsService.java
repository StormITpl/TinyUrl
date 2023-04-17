package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsMapper;
import pl.stormit.tinyurl.exception.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlAnalyticsService {

    public static final long FIRST_CLICK_ON_SHORT_URL = 1L;

    private final UrlAnalyticsRepository urlAnalyticsRepository;

    private final UrlAnalyticsMapper urlAnalyticsMapper;

    public List<UrlAnalytics> getAnalyticsByUrlId(UUID urlId) {
        if (!urlAnalyticsRepository.existsById(urlId)) {
            throw new ApiException("Change the request your id does not exist!");
        }
        return urlAnalyticsRepository.findAllByUrlId(urlId);
    }

    public List<UrlAnalytics> getAllAnalytics() {
        return urlAnalyticsRepository.findAll();
    }

    public UrlAnalyticsDto clickCounter(Url url) {

        UUID urlId = url.getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
        urlAnalytics.setUrl(url);

        return urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
    }

    private Long checkClicksAmountOnShortUrl(UUID urlId) {
        long incMaxClickValue;
        Long maxClickValue = urlAnalyticsRepository.findMaxClickOnShortUrlByUrlId(urlId);

        if (maxClickValue == null) {
            return FIRST_CLICK_ON_SHORT_URL;
        } else {
            incMaxClickValue = maxClickValue.longValue();
            incMaxClickValue++;
            return incMaxClickValue;
        }
    }

    public UrlAnalyticsDto ipLocalization(Url url, HttpServletRequest servletRequest){

        UrlAnalytics urlAnalytics = new UrlAnalytics();
        String addressIp = servletRequest.getRemoteAddr();
        getIpLocalization(addressIp);

        return urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
    }

    private String getIpLocalization(String addressIp) throws IOException, GeoIp2Exception {

        File database;
        database = new File("src/main/resources/localizationdb/GeoLite2-City.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName(addressIp);

        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();

        return null;
    }
}
