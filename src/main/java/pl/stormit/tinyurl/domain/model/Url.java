package pl.stormit.tinyurl.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Url {

    @Id
    private Long id;
    private String longUrl;
    private String shortUrl;
}
