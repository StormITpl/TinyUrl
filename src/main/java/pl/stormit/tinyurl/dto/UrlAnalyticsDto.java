package pl.stormit.tinyurl.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlAnalyticsDto {

    private UUID id;

    @NotBlank
    private Long totalClicks;

    private String countryLocalization;

    private String isoCode;

    private String cityLocalization;

    @NotBlank
    private Instant clickDate;
}
