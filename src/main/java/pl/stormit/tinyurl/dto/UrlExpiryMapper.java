package pl.stormit.tinyurl.dto;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.UrlExpiry;

@Mapper(componentModel = "spring")
public interface UrlExpiryMapper {

    UrlExpiryDto mapUrlExpiryEntityToUrlExpiryDto(UrlExpiry urlExpiry);
}
