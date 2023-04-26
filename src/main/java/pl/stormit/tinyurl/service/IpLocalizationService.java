package pl.stormit.tinyurl.service;

import com.maxmind.geoip2.DatabaseReader;
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
