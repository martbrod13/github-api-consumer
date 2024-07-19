# GitHub API Consumer

## Opis

Aplikacja Java Spring Boot, która korzysta z GitHub API do pobierania listy repozytoriów użytkownika, które nie są forkiem. Dla każdego repozytorium zwracane są nazwa repozytorium, login właściciela oraz nazwa gałęzi i ostatnie commit SHA.

### 2. Klonowanie repozytorium

```sh
git clone <https://github.com/martbrod13/github-api-consumer.git>
cd github-api-consumer

mvn clean install
mvn spring-boot:run


# Testowanie
curl -H "Accept: application/json" http://localhost:8080/api/github/users/octocat/repos

g i t h u b - a p i - c o n s u m e r  
 #   g i t h u b - a p i - c o n s u m e r  
 #   g i t h u b - a p i - c o n s u m e r  
 #   g i t h u b - a p i - c o n s u m e r  
 #   g i t h u b - a p i - c o n s u m e r  
 