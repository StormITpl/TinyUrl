package pl.stormit.tinyurl.domain.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "urls")
@ToString
@Setter
@Getter
public class Url {
    @Id
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String longUrl;

    @NotNull
    @Column(unique = true, nullable = false)
    private String shortUrl;

    private LocalDate creationDate = LocalDate.now();

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "url")
    private UrlAnalytics urlAnalytics;

    public Url(UUID id, String longUrl, String shortUrl) {
        this.id = UUID.randomUUID();
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public Url() {
        this.id = UUID.randomUUID();
    }

    public Url(String longUrl, String shortUrl) {
        this();
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
