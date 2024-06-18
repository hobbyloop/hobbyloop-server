package com.example.ticketservice.common.entity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
public class CustomAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return Optional.empty();
        }

        HttpServletRequest request = attributes.getRequest();

        Optional<String> id = Optional.ofNullable(request.getHeader("id"));
        Optional<String> role = Optional.ofNullable(request.getHeader("role"));

        if (id.isPresent() && role.isPresent()) {
            return Optional.of(id.get() + " " + role.get());
        }
        return Optional.of("system");
    }
}
