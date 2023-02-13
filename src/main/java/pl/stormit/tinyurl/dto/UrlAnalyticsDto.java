package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UrlAnalyticsDto {

    private UUID id;

    @NotBlank
    private Long totalClicks;

    private String localization;

    @NotBlank
    private Instant clickDate;
}