package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

    @NotBlank
    private String longUrl;

    private String shortUrl;

    private UrlAnalyticsDto analytics;
}
