package pl.kaitoudev.atiperademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configures the Rest-related Beans.
 */
@Configuration
public class RestConfig {
    /**
     * Provides a {@link RestTemplate}
     * @return A {@link RestTemplate} instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
