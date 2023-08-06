package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

    private UUID id;

    @NotBlank
    private String longUrl;

    private String shortUrl;

    private UrlAnalyticsDto analytics;

    private UrlExpiryDto expiry;
}
