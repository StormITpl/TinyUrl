package pl.stormit.tinyurl.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
public class UrlExpiry {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private Instant creationDate = Instant.now();

    private Instant expirationDate;

    @Column(nullable = false)
    private Boolean isPremium = false;


    @OneToOne(cascade = CascadeType.ALL)
    private Url url;
}
