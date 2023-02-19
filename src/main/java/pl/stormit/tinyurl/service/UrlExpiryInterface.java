package pl.stormit.tinyurl.service;

import pl.stormit.tinyurl.domain.model.UrlExpiry;

public interface UrlExpiryInterface {

    UrlExpiry isAccountPremium(UrlExpiry urlExpiry);

    int TWO_WEEKS = 1209600;
}
