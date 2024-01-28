package pl.stormit.tinyurl.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
