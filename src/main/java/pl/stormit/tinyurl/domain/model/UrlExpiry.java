package pl.stormit.tinyurl.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_expiry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlExpiry {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private Instant creationDate = Instant.now();

    private Instant expirationDate;

    private Boolean isPremium = false;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    private Url url;
}
