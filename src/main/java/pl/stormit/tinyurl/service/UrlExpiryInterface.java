package pl.stormit.tinyurl.service;

import pl.stormit.tinyurl.domain.model.UrlExpiry;

public interface UrlExpiryInterface {

    boolean isAccountPremium(UrlExpiry urlExpiry);

    int TWO_WEEKS_IN_SECONDS = 1209600;
}
