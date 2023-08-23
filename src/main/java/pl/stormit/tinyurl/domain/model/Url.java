package pl.stormit.tinyurl.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "urls")
@Data
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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "url", fetch = FetchType.LAZY)
    private List<UrlAnalytics> urlAnalytics;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "url")
    private UrlExpiry urlExpiry;

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
