package pl.stormit.tinyurl.domain.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "urls")
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String longUrl;

    @NotNull
    @Column(unique = true, nullable = false)
    private String shortUrl;

    private LocalDate creationDate = LocalDate.now();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "url")
    private List<UrlAnalytics> urlAnalytics;

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
