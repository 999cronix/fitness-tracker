package de.htwberlin.fitness_tracker;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Lehnt Anfragen mit zu grossem Body ab (Status 413), bevor der Server sie liest.
 * Ein Trainingseintrag ist nur wenige hundert Zeichen gross. Ohne Grenze koennte jemand
 * riesige Anfragen schicken und den kleinen Server (512 MB RAM) auslasten.
 */
@Component
public class RequestSizeLimitFilter extends OncePerRequestFilter {

    /** Groesste erlaubte Body-Groesse in Bytes. */
    static final long MAX_BODY_BYTES = 10_000;

    /**
     * Prueft den Header Content-Length. Ist der Body groesser als erlaubt, geht die Anfrage
     * nicht weiter, sonst wird sie normal bearbeitet.
     *
     * @param request  die eingehende Anfrage
     * @param response die Antwort, die an den Browser geht
     * @param chain    die restliche Filterkette und am Ende der Controller
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        if (request.getContentLengthLong() > MAX_BODY_BYTES) {
            response.setStatus(413); // 413 = Content Too Large
            return;
        }
        chain.doFilter(request, response);
    }
}
