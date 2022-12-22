package pl.stormit.tinyurl.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_expiry")
@Getter
@Setter
@ToString
public class UrlExpiry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Instant creationDate = Instant.now();

    private Instant expirationDate;

    @Column(nullable = false)
    private Boolean isPremium = false;

    @OneToOne
    private Url url;
}
