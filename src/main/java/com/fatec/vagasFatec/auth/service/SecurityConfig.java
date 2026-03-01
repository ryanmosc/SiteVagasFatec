package com.fatec.vagasFatec.auth.service;

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
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // mantém seu CORS
                .authorizeHttpRequests(auth -> auth

                        // === Rotas públicas / sem autenticação ===
                        .requestMatchers("/api/auth/**").permitAll()                          // login, register auth
                        .requestMatchers(HttpMethod.POST, "/api/candidatos").permitAll()      // cadastro de candidato
                        .requestMatchers(HttpMethod.POST, "/api/empresas").permitAll()        // cadastro de empresa
                        .requestMatchers(HttpMethod.GET, "/api/vagas").permitAll()            // listar vagas abertas (público)
                        .requestMatchers(HttpMethod.GET, "/api/vagas/**").permitAll()         // detalhes de vaga aberta (se existir)

                        // === Rotas de CANDIDATO (ações próprias) ===
                        .requestMatchers("/api/candidatos/perfil").hasRole("CANDIDATO")       // ver/atualizar meu perfil
                        .requestMatchers("/api/candidaturas/minhas").hasRole("CANDIDATO")     // minhas candidaturas
                        .requestMatchers(HttpMethod.POST, "/api/candidaturas/vaga/**").hasRole("CANDIDATO")   // se candidatar
                        .requestMatchers(HttpMethod.PATCH, "/api/candidaturas/vaga/*/desistir").hasRole("CANDIDATO")
                        .requestMatchers(HttpMethod.GET, "/api/candidaturas/vaga/minhas/*").hasRole("CANDIDATO")

                        // === Rotas de EMPRESA (ações próprias) ===
                        .requestMatchers(HttpMethod.POST, "/api/vagas").hasRole("EMPRESA")               // criar vaga
                        .requestMatchers("/api/vagas/minhas").hasRole("EMPRESA")                         // minhas vagas
                        .requestMatchers("/api/vagas/{id}/**").hasRole("EMPRESA")                        // editar, encerrar, reabrir, deletar vaga
                        .requestMatchers("/api/candidaturas/{idCandidatura}/**").hasRole("EMPRESA")     // status, observação, etc.

                        // === Rotas administrativas (se você tiver ROLE_ADMIN no futuro) ===
                        // .requestMatchers("/api/candidatos", "/api/empresas", "/api/candidaturas").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PATCH, "/api/candidatos/**/desativar").hasRole("ADMIN")

                        // Tudo o mais exige autenticação (fallback seguro)
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
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
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