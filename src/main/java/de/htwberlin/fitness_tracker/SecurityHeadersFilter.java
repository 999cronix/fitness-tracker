package de.htwberlin.fitness_tracker;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Setzt bei jeder Antwort einige Sicherheits-Header.
 * Ein Filter laeuft vor dem Controller und kann jede Anfrage und Antwort veraendern.
 * Die Header sagen dem Browser, wie er die Antwort behandeln soll:
 * - nosniff: Der Browser darf den Inhaltstyp nicht raten (JSON bleibt JSON).
 * - X-Frame-Options DENY: Die Antwort darf nicht in einem fremden Frame eingebettet werden.
 * - Referrer-Policy no-referrer: Beim Weitergeben wird keine Herkunfts-Adresse mitgeschickt.
 */
@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {

    /**
     * Fuegt die Header hinzu und gibt die Anfrage dann an die naechste Stelle weiter.
     *
     * @param request  die eingehende Anfrage
     * @param response die Antwort, die an den Browser geht
     * @param chain    die restliche Filterkette und am Ende der Controller
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Referrer-Policy", "no-referrer");
        chain.doFilter(request, response);
    }
}
