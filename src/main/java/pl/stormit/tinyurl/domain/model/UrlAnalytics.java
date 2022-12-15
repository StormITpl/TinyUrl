package pl.stormit.tinyurl.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table (name = "url_analytics")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UrlAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
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