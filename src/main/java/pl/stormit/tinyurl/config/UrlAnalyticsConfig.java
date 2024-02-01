package pl.stormit.tinyurl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

@Configuration
public class UrlAnalyticsConfig {

    @Value("${tiny.amount-of-popular-urls.offset}")
    private int popularUrlsStartIndex;

    @Value("${tiny.amount-of-popular-urls.limit}")
    private int popularUrlsPageSize;

    @Bean
    public PageRequest amountOfPopularUrls() {
        return PageRequest.of(popularUrlsStartIndex, popularUrlsPageSize);
    }
}
