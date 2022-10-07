package pl.stormit.tinyurl.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.domain.repository.UrlRepository;

import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

@Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<Url> getUrl(){
        return urlRepository.findAll();
    }
}
