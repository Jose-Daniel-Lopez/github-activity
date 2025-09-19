package com.github.Jose_Daniel_Lopez.github_activity_cli;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventFormatter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class GithubActivityCliApplication implements CommandLineRunner {

	private final RestTemplate restTemplate;

	public GithubActivityCliApplication() {
		this.restTemplate = new RestTemplate();
		restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().set("User-Agent", "SpringBootGitHubCLI/1.0");
            return execution.execute(request, body);
        }));
	}

	public static void main(String[] args) {
		SpringApplication.run(GithubActivityCliApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length == 0) {
			System.out.println("Please provide a GitHub username as an argument.");
			return;
		}

		String username = args[0];
		System.out.println("Fetching GitHub activity for user: " + username);

		String url = "https://api.github.com/users/" + username + "/events";
		System.out.println("GitHub Events URL: " + url);

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();

			if (events == null || events.length == 0) {
				System.out.println("No recent activity found.");
				return;
			}

			for (GitHubEvent event : events) {
				String formattedEvent = EventFormatter.format(event);
				System.out.println(formattedEvent);
			}

		} catch (HttpClientErrorException e) {
			System.out.println("Error fetching events: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
		}
	}
}