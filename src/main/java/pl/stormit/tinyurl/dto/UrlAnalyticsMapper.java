package pl.stormit.tinyurl.dto;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;

@Mapper(componentModel = "spring")
public interface UrlAnalyticsMapper {

    UrlAnalyticsDto mapUrlAnalyticsEntityToUrlAnalyticsDto(UrlAnalytics urlAnalytics);
}
