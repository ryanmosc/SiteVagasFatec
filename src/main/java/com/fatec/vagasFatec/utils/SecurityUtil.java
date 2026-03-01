package com.fatec.vagasFatec.utils;  // ou no pacote que preferir

import com.fatec.vagasFatec.auth.service.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {


    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se existe autenticação e se está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Nenhum usuário autenticado na requisição atual");
        }

        Object principal = authentication.getPrincipal();

        // Caso mais comum: o principal é nosso CustomUserDetails
        if (principal instanceof CustomUserDetailsService userDetails) {
            return userDetails.getId();
        }

        // Caso fallback (menos comum): se for String (username), mas não recomendado
        if (principal instanceof UserDetails userDetails) {
            // Se você não tiver ID no UserDetails padrão, pode lançar exceção ou tratar de outra forma
            throw new RuntimeException("Usuário autenticado sem ID disponível (use CustomUserDetails)");
        }

        // Qualquer outro tipo inesperado
        throw new RuntimeException("Tipo de principal não suportado: " + principal.getClass().getName());
    }

    /**
     * Versão alternativa que retorna o email (username) do JWT, caso precise em algum lugar
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new RuntimeException("Nenhum usuário autenticado");
    }
}