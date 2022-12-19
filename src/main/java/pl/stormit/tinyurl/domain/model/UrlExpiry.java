package pl.stormit.tinyurl.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "url_expiry")
@Getter
@Setter
@ToString
public class UrlExpiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
}
