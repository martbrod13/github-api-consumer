package com.example.githubapiconsumer.controller;

import com.example.githubapiconsumer.service.GithubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<?> getUserRepos(@PathVariable String username) {
        return githubService.getRepositories(username);
    }
}
