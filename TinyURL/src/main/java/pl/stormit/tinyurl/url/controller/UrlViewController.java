package pl.stormit.tinyurl.url.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/url")
public class UrlViewController {

    @GetMapping
    public String ShortedURL(){
        return "this should be shorted URL";
    }
}
