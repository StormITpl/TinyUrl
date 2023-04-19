package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.domain.repository.UrlAnalyticsRepository;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;
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

    public void setAnalitycsData(Url url, HttpServletRequest servletRequest) {

        String addressIp = servletRequest.getRemoteAddr();

        UUID urlId = url.getId();
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setClickDate(Instant.now());
        urlAnalytics.setTotalClicks(checkClicksAmountOnShortUrl(urlId));
        urlAnalytics.setCountryLocalization(getIpLocalization(addressIp).getCountryLocalization());
        urlAnalytics.setCountryLocalization(getIpLocalization(addressIp).getIsoCode());
        urlAnalytics.setCityLocalization(getIpLocalization(addressIp).getCityLocalization());
        urlAnalytics.setUrl(url);

        urlAnalyticsMapper.mapUrlAnalyticsEntityToUrlAnalyticsDto(urlAnalyticsRepository.save(urlAnalytics));
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

    private UrlAnalyticsLocalizationDto getIpLocalization(String addressIp) {

        String countryName = null;
        String isoCode = null;
        String cityName = null;

        try {
            InetAddress ipAddress = InetAddress.getByName(addressIp);

            DatabaseReader database = new DatabaseReader.
                    Builder(new File("src/main/resources/localizationdb/GeoLite2-City.mmdb")).build();
            CityResponse response = database.city(ipAddress);

            countryName = response.getCountry().getName();
            isoCode = response.getCountry().getIsoCode();
            cityName = response.getCity().getName();

        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }

        return new UrlAnalyticsLocalizationDto(countryName, isoCode, cityName);
    }
}
