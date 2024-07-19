package com.example.githubapiconsumer.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {

    private static final String GITHUB_API_URL = "https://api.github.com/users/%s/repos";
    private static final String GITHUB_BRANCHES_URL = "https://api.github.com/repos/%s/%s/branches";

    private final RestTemplate restTemplate;

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> getRepositories(String username) {
        try {
            String url = String.format(GITHUB_API_URL, username);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<JsonNode> nonForkRepos = new ArrayList<>();
                for (JsonNode repo : response.getBody()) {
                    if (!repo.get("fork").asBoolean()) {
                        String repoName = repo.get("name").asText();
                        String ownerLogin = repo.get("owner").get("login").asText();
                        List<JsonNode> branches = getBranches(ownerLogin, repoName);
                        ((ObjectNode) repo).set("branches", JsonNodeFactory.instance.arrayNode().addAll(branches));
                        nonForkRepos.add(repo);
                    }
                }
                return ResponseEntity.ok(nonForkRepos);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
        }
    }

    private List<JsonNode> getBranches(String owner, String repo) {
        String url = String.format(GITHUB_BRANCHES_URL, owner, repo);
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        List<JsonNode> branches = new ArrayList<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            for (JsonNode branch : response.getBody()) {
                branches.add(branch);
            }
        }
        return branches;
    }

    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // getters and setters
    }
}
