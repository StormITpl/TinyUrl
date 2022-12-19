package pl.stormit.tinyurl.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table (name = "url_analytics")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UrlAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @PositiveOrZero
    private Long totalClicks;

    private String localization;

    @NotBlank
    private Instant clickDate;

    @OneToOne
    private Url url;

    UrlAnalytics(final Long totalClicks, final String localization, final Instant clickDate) {
        this.totalClicks = totalClicks;
        this.localization = localization;
        this.clickDate = Instant.now();
    }
}