package com.kosa.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = "";

        if (authentication != null) {
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }
}
