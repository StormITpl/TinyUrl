package pl.stormit.tinyurl.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostPopularUrlResult {
    private Url url;
    private Long totalClicks;
}
