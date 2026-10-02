package com.banco.xyz.bff_atm.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/bff-atm/transacciones/**").hasAuthority("SCOPE_cuentas.read")
                        .requestMatchers("/api/bff-atm/transaccion/**").hasAuthority("SCOPE_cuentas.read")
                        .requestMatchers("/api/bff-atm/estadoCuentas/**").hasAuthority("SCOPE_cuentas.read")
                        .requestMatchers("/api/bff-atm/estadoCuenta/**").hasAuthority("SCOPE_cuentas.read")
                        .requestMatchers("/api/bff-atm/intereses/**").hasAuthority("SCOPE_cuentas.read")
                        .requestMatchers("/api/bff-atm/interes/**").hasAuthority("SCOPE_cuentas.read")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
