package pl.stormit.tinyurl.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
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


    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "url")
    private UrlExpiry urlExpiry;

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}
