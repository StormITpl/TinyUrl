package pl.stormit.tinyurl.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlAnalytics {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @PositiveOrZero
    private Long totalClicks;

    private String countryLocalization;

    private String isoCode;

    private String cityLocalization;

    @NotNull
    private Instant clickDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Url url;
}
