package pl.stormit.tinyurl.domain.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stormit.tinyurl.domain.model.Url;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {

}
