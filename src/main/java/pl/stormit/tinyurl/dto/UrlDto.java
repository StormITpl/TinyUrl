package pl.stormit.tinyurl.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
