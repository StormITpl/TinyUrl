package pl.stormit.tinyurl.mappers;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.Url;
import pl.stormit.tinyurl.dto.UrlDto;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    UrlDto mapUrlEntityToUrlDto(Url url);
}
