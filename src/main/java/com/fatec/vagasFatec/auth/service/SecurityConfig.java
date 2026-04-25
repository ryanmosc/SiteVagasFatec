package com.fatec.vagasFatec.auth.service;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@SecurityScheme(name = SecurityConfig.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    public static final String SECURITY = "BearerAuth";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth

                        // === Rotas públicas ===
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/candidatos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/empresas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vagas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vagas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/candidatos/validar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/candidatos/validar/reenviar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/candidatos/validar/empresa").permitAll()
                        .requestMatchers("/api/candidatos/perfil/todos/**").permitAll()

                        // === Rotas de currículo — MAIS ESPECÍFICAS PRIMEIRO ===

                        // Empresa visualiza currículo de candidato via candidatura (GET com ID na rota)
                        .requestMatchers(HttpMethod.GET, "/api/candidatos/perfil/curriculo/visualizar/*/candidatura").hasRole("EMPRESA")

                        // Empresa visualiza foto do candidato via candidatura (GET com ID na rota)
                        .requestMatchers(HttpMethod.GET, "/api/candidatos/perfil/foto/visualizar/*/candidatura").hasRole("EMPRESA")

                        // Candidato visualiza o próprio currículo
                        .requestMatchers(HttpMethod.GET, "/api/candidatos/perfil/curriculo/visualizar").hasRole("CANDIDATO")

                        // Candidato visualiza a sua propria foto de perfil
                        .requestMatchers(HttpMethod.GET, "/api/candidatos/perfil/foto/visualizar").hasRole("CANDIDATO")

                        // Candidato faz upload do currículo (PATCH)
                        .requestMatchers(HttpMethod.PATCH, "/api/candidatos/perfil/curriculo").hasRole("CANDIDATO")

                        // Candidato faz upload de sua foto de perfil (PATCH)
                        .requestMatchers(HttpMethod.PATCH, "/api/candidatos/perfil/foto").hasRole("CANDIDATO")

                        // === Rotas de CANDIDATO ===
                        .requestMatchers("/api/candidatos/perfil").hasRole("CANDIDATO")
                        .requestMatchers("/api/candidaturas/minhas").hasRole("CANDIDATO")
                        .requestMatchers(HttpMethod.POST, "/api/candidaturas/vaga/**").hasRole("CANDIDATO")
                        .requestMatchers(HttpMethod.PATCH, "/api/candidaturas/vaga/*/desistir").hasRole("CANDIDATO")
                        .requestMatchers(HttpMethod.GET, "/api/candidaturas/vaga/minhas/*").hasRole("CANDIDATO")

                        // === Rotas de EMPRESA ===
                        .requestMatchers(HttpMethod.POST, "/api/vagas").hasRole("EMPRESA")
                        .requestMatchers("/api/vagas/minhas").hasRole("EMPRESA")
                        .requestMatchers("/api/vagas/{id}/**").hasRole("EMPRESA")
                        .requestMatchers("/api/candidaturas/{idCandidatura}/**").hasRole("EMPRESA")
                        .requestMatchers(HttpMethod.GET, "/api/empresas/perfil").hasRole("EMPRESA")

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}