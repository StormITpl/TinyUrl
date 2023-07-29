package pl.stormit.tinyurl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
