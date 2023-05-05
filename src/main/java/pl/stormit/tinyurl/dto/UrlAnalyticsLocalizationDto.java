package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlAnalyticsLocalizationDto {

    private String countryLocalization;

    private String isoCode;

    private String cityLocalization;

    public UrlAnalyticsLocalizationDto() {
    }
}
