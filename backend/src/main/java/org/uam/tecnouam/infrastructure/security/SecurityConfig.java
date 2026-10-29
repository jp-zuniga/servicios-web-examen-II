package org.uam.tecnouam.infrastructure.security;

import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cuentas de prueba para la demostración (ver README).
    @Bean UserDetailsService usuarios(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password(encoder.encode("Admin123!"))
                .roles("ADMIN")
                .build(),
            User.withUsername("instructor")
                .password(encoder.encode("Instructor123!"))
                .roles("USER")
                .build()
        );
    }

    @Bean AuthenticationManager authenticationManager(
        UserDetailsService usuarios,
        PasswordEncoder encoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarios);
        provider.setPasswordEncoder(encoder);

        return new ProviderManager(provider);
    }

    @Bean SecretKey jwtSecretKey(@Value("${JWT_SECRET_BASE64:}") String base64) {
        if (base64.isBlank()) {
            throw new IllegalStateException(
                "Configure la variable de entorno JWT_SECRET_BASE64.");
        }

        byte[] clave = Base64.getDecoder().decode(base64.trim());

        if (clave.length < 32) {
            throw new IllegalStateException(
                "La clave HS256 debe tener al menos 32 bytes.");
        }

        return new SecretKeySpec(clave, "HmacSHA256");
    }

    @Bean JwtEncoder jwtEncoder(SecretKey clave) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(clave));
    }

    @Bean JwtDecoder jwtDecoder(SecretKey clave) {
        return NimbusJwtDecoder.withSecretKey(clave)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }

    @Bean JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(roles);

        return converter;
    }

    @Bean CorsConfigurationSource corsConfigurationSource(
        @Value("${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:3000}")
        List<String> origenes
    ) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origenes);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }

    @Bean SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationConverter converter
    ) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/error")
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/equipos", "/api/equipos/**")
                .hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/equipos", "/api/equipos/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/prestamos", "/api/prestamos/**")
                .hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/prestamos", "/api/prestamos/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(
                converter)))
            .build();
    }
}
