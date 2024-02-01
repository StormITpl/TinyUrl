package pl.stormit.tinyurl.mappers;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.UrlAnalytics;
import pl.stormit.tinyurl.dto.UrlAnalyticsDto;

@Mapper(componentModel = "spring")
public interface UrlAnalyticsMapper {

    UrlAnalyticsDto mapUrlAnalyticsEntityToUrlAnalyticsDto(UrlAnalytics urlAnalytics);
}
