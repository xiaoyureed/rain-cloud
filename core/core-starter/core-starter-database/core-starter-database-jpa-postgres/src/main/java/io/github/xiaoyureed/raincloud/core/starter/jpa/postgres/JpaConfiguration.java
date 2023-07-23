package io.github.xiaoyureed.raincloud.core.starter.jpa.postgres;//package io.github.xiaoyureed.raincloud.core.starter.datasource.jpa;
//
//import java.util.Objects;
//import java.util.Optional;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Configuration
//@EnableJpaAuditing
//public class JpaConfiguration {
//    @Bean
//    public AuditorAware<Long> auditorAware() {
//        return () -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
//                return Optional.empty();
//            }
//            var currentUser = (User) authentication.getPrincipal();
//            return Optional.of(currentUser.getId());
//        };
//    }
//}
