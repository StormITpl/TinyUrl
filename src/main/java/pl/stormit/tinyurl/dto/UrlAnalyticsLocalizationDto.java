package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlAnalyticsLocalizationDto {

    private String countryLocalization;

    private String isoCode;

    private String cityLocalization;
}
