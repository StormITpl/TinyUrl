package pl.stormit.tinyurl.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class UrlAnalyticsDto {

    @NotBlank
    private Long totalClicks;

    private String localization;

    @NotBlank
    private Instant clickDate;

    private Long urlId;

    @NotNull
    private String shortUrl;
}