package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class IpLocalizationService {

    public UrlAnalyticsLocalizationDto getIpLocalization(String addressIp) {

        InetAddress ipAddress;
        UrlAnalyticsLocalizationDto setLocalization = new UrlAnalyticsLocalizationDto(null, null, null);

        try {
            ipAddress = InetAddress.getByName(addressIp);

            DatabaseReader database = new DatabaseReader.
                    Builder(new File("src/main/resources/localizationdb/GeoLite2-City.mmdb")).build();
            CityResponse response = database.city(ipAddress);

            if (response != null) {
                setLocalization.setCountryLocalization(response.getCountry().getName());
                setLocalization.setIsoCode(response.getCountry().getIsoCode());
                setLocalization.setCityLocalization(response.getCity().getName());
            } else {
                throw new AddressNotFoundException("Failed to get location for IP address: " + addressIp);
            }

        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException("Failed to get location for IP address: " + addressIp, e);
        }
        return setLocalization;
    }
}
