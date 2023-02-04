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
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private Instant creationDate = Instant.now();

    private Instant expirationDate = creationDate.plusSeconds(1209600);

    @Column(nullable = false)
    private Boolean isPremium = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Url url;
}
