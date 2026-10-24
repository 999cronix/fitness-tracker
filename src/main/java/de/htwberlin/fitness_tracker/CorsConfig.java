package de.htwberlin.fitness_tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Erlaubt dem Vue-Frontend (eigener Ursprung, z. B. localhost:5173) den Zugriff auf die API.
 * CORS (Cross-Origin Resource Sharing) ist die Browser-Regel, die festlegt, welche
 * anderen Webseiten Daten von diesem Server lesen duerfen.
 * Die erlaubten Ursprünge kommen aus der Property app.cors.allowed-origins.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Kommagetrennte Liste erlaubter Ursprünge, z. B. "http://localhost:5173,https://meine-app.onrender.com"
    private final String[] allowedOrigins;

    public CorsConfig(@Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * Registriert die CORS-Regeln fuer alle Pfade unter "/".
     * Erlaubt sind GET, POST und OPTIONS. OPTIONS ist die Vorab-Anfrage (Preflight),
     * mit der der Browser vorher nachfragt. Als Header ist nur Content-Type erlaubt,
     * damit das Frontend JSON per POST schicken kann.
     *
     * @param registry Sammelstelle, in der die CORS-Regeln eingetragen werden
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("Content-Type");
    }
}
