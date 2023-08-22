package pl.stormit.tinyurl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlExpiryDto {

    @NotBlank
    private Instant creationDate;

    private Instant expirationDate;

    @NotBlank
    private Boolean isPremium;

    @NotNull
    private String shortUrl;
}
