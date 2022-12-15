package pl.stormit.tinyurl.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table (name = "url_analytics")
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
}