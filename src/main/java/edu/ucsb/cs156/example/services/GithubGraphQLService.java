package edu.ucsb.cs156.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.GraphQLClientException;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import edu.ucsb.cs156.example.errors.GraphQLResponseErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GithubGraphQLService {
  @Autowired
  private OAuth2AuthorizedClientService clientService;

  @Autowired
  private ObjectMapper mapper;

  private String getAccessTokenFromAuth(OAuth2AuthenticationToken token) {
    OAuth2AuthorizedClient client =
      clientService.loadAuthorizedClient(
        token.getAuthorizedClientRegistrationId(),
        token.getName()
      );

    return client.getAccessToken().getTokenValue();
  }

  private String getCurrentAccessToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof OAuth2AuthenticationToken)) {
      throw new IllegalStateException(
        "Not logged in, or logged in without an OAuth token. Authentication was %s".formatted(authentication)
      );
    }

    return getAccessTokenFromAuth(((OAuth2AuthenticationToken) authentication));
  }

  public GraphQLResponse executeGraphQLQuery(String query) {
    return executeGraphQLQuery(query, Map.of());
  }

  public GraphQLResponse executeGraphQLQuery(String query, Map<String, Object> variables) {
    String accessToken = getCurrentAccessToken();

    WebClient webClient =
      WebClient.builder()
        .baseUrl("https://api.github.com/graphql")
        .defaultHeaders(h -> h.setBearerAuth(accessToken))
        .build();

    WebClientGraphQLClient client = MonoGraphQLClient.createWithWebClient(webClient);

    log.info("GraphQL Query:\n" + query);

    GraphQLResponse response = client.reactiveExecuteQuery(query, variables).block();

    if (response.hasErrors()) {
      throw new GraphQLResponseErrorException(response);
    }

    try {
      String outputStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getData());
      log.info("GraphQL Response:\n" + outputStr);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return response;
  }

  // public String projectId(String owner, String repo, int projNum) {
  //   GraphQLResponse result = this.executeGraphQLQuery("""
  //     query($owner: String!, $repo: String!, $projNum: Int!){
  //       repository(owner: $owner, name: $repo) {
  //           project(number: $projNum) {
  //           id
  //           name
  //           }
  //       }
  //       }
  //     """,
  //     Map.of(
  //       "owner", owner,
  //       "repo", repo,
  //       "projNum", projNum
  //     ));
    
  //   return result.extractValue("repository.project.id");
  // }
}
