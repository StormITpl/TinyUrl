package pl.stormit.tinyurl.domain.model;

import lombok.Data;

@Data
public class MostPopularUrlResult {
    private Url url;
    private Long totalClicks;
}
