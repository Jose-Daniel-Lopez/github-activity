package com.github.Jose_Daniel_Lopez.github_activity_cli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up the {@link WebClient} bean.
 * <p>
 * This bean is used throughout the application to make HTTP requests to external services,
 * such as the GitHub API. {@code WebClient} is the modern, non-blocking, reactive-capable HTTP client
 * introduced to replace the deprecated {@code RestTemplate}.
 * </p>
 * <p><strong>Important:</strong> GitHub API requires a valid {@code User-Agent} header.
 * This is configured by default in the provided bean.</p>
 * <p><em>Future considerations:</em> For production use, consider adding:
 * <ul>
 *   <li>Timeout configuration</li>
 *   <li>Retry/backoff policies</li>
 *   <li>Logging of requests/responses (via {@code ExchangeFilterFunction})</li>
 *   <li>Metrics collection (e.g., via Micrometer)</li>
 * </ul>
 * </p>
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates and configures a {@link WebClient} bean for making HTTP requests.
     * <p>
     * This instance is managed by the Spring container and can be autowired into any component
     * requiring HTTP client capabilities. Although reactive by nature, it can be used in
     * blocking mode via {@code .block()} if needed (not recommended for high-throughput scenarios).
     * </p>
     * <p><strong>Note:</strong> This bean is singleton-scoped and thread-safe — safe to inject and reuse.</p>
     *
     * @return a pre-configured {@link WebClient} instance with required headers
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "SpringBootGitHubCLI/1.0")
                .build();
    }
}