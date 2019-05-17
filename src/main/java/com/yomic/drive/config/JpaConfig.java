package com.yomic.drive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (p instanceof String) return Optional.of((String)p);
        UserDetails user = (UserDetails) p;
        if (user == null || user.getUsername() == null) return Optional.of("system");
        return Optional.of(user.getUsername());
    }
}
