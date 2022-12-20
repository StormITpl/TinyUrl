package pl.stormit.tinyurl.domain.model;

import com.sun.istack.NotNull;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "urls")
@ToString
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Url() {
        this.id = UUID.randomUUID();
    }

    public Url(String longUrl, String shortUrl) {
        this();
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
