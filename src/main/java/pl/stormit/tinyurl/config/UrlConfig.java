package pl.stormit.tinyurl.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;

import java.util.List;

@Configuration
public class UrlConfig {

    @Bean
    CommandLineRunner commandLineRunner(UrlRepository repository){
        return args -> {
            Url google = new Url(
                    "www.google.com",
                    "def456");
            Url yahoo = new Url(
                    "www.yahoo.com",
                    "abc123"
            );
            repository.saveAll(List.of(google, yahoo));
        };
    }
}
