package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.dto.UrlAnalyticsLocalizationDto;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class IpLocalizationService {

    private final DatabaseReader database;

    public IpLocalizationService() {
        this.database = GeoLiteDatabaseLoader.getInstance().getDatabase();
    }

    public UrlAnalyticsLocalizationDto getIpLocalization(String addressIp) {

        String country;
        String isoCode;
        String city;
        InetAddress ipAddress;
        UrlAnalyticsLocalizationDto localizationDto = new UrlAnalyticsLocalizationDto();

        if (database == null) {
            throw new IllegalStateException("GeoLite2 database not initialized");
        }

        try {
            ipAddress = InetAddress.getByName(addressIp);

            CityResponse response = database.city(ipAddress);
            country = response.getCountry().getName();
            isoCode = response.getCountry().getIsoCode();
            city = response.getCity().getName();

            if (country != null && isoCode != null && city != null) {
                localizationDto.setCountryLocalization(country);
                localizationDto.setIsoCode(isoCode);
                localizationDto.setCityLocalization(city);
            } else {
                throw new AddressNotFoundException("Failed to get location for IP address: " + addressIp);
            }

        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException("Failed to get location for IP address: " + addressIp, e);
        }
        return localizationDto;
    }
}
