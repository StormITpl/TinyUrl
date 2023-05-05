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

    public static final String LOCALIZATION_DB_GEO_LITE = "src/main/resources/localizationdb/GeoLite2-City.mmdb";

    public UrlAnalyticsLocalizationDto getIpLocalization(String addressIp) {

        String country;
        String isoCode;
        String city;
        InetAddress ipAddress;
        UrlAnalyticsLocalizationDto setLocalization = new UrlAnalyticsLocalizationDto();

        try {
            ipAddress = InetAddress.getByName(addressIp);

            DatabaseReader database = new DatabaseReader.
                    Builder(new File(LOCALIZATION_DB_GEO_LITE)).build();
            CityResponse response = database.city(ipAddress);
            country = response.getCountry().getName();
            isoCode = response.getCountry().getIsoCode();
            city = response.getCity().getName();

            if (country != null && isoCode != null && city != null) {
                setLocalization.setCountryLocalization(country);
                setLocalization.setIsoCode(isoCode);
                setLocalization.setCityLocalization(city);
            } else {
                throw new AddressNotFoundException("Failed to get location for IP address: " + addressIp);
            }

        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException("Failed to get location for IP address: " + addressIp, e);
        }
        return setLocalization;
    }
}
