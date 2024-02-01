package pl.stormit.tinyurl.mappers;

import org.mapstruct.Mapper;
import pl.stormit.tinyurl.domain.model.UrlExpiry;
import pl.stormit.tinyurl.dto.UrlExpiryDto;

@Mapper(componentModel = "spring")
public interface UrlExpiryMapper {

    UrlExpiryDto mapUrlExpiryEntityToUrlExpiryDto(UrlExpiry urlExpiry);
}
