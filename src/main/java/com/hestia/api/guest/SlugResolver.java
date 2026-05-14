package com.hestia.api.guest;

import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SlugResolver implements HandlerInterceptor {

    private final WeddingRepository weddingRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVars != null && pathVars.containsKey("slug")) {
            String slug = pathVars.get("slug");
            Wedding wedding = weddingRepository.findBySlugAndIsActiveTrue(slug)
                    .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));
            request.setAttribute("weddingId", wedding.getId());
        }

        return true;
    }
}
