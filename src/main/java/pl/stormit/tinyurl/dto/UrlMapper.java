package pl.stormit.tinyurl.dto;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.Url;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    UrlDto mapUrlEntityToUrlDto(Url url);
}
