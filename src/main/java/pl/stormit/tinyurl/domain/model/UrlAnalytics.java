package pl.stormit.tinyurl.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrlAnalytics {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @PositiveOrZero
    private Long totalClicks;

    private String localization;

    @NotNull
    private Instant clickDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Url url;
}