package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;

import edu.ucsb.cs156.example.ControllerTestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class GithubServiceTests extends ControllerTestCase {
//   @MockBean
//   GithubGraphQLService mockGithubApi;

// GraphQLResponse(json={"data":{"repository":{"project":{"id":"PRO_kwLOG0U47s4A11-W","name":"W22 Play Page"}}}}, 
//     headers=[Server:"GitHub.com", Date:"Mon, 25 Apr 2022 23:19:04 GMT", Content-Type:"application/json; charset=utf-8", 
//         Transfer-Encoding:"chunked", X-OAuth-Scopes:"admin:org, public_repo, read:user, user:email", X-Accepted-OAuth-Scopes:"repo", 
//         x-oauth-client-id:"1498bd829e8d1195c8d7", X-GitHub-Media-Type:"github.v4", X-RateLimit-Limit:"5000", X-RateLimit-Remaining:"4971", 
//         X-RateLimit-Reset:"1650929778", X-RateLimit-Used:"29", X-RateLimit-Resource:"graphql", 
//         Access-Control-Expose-Headers:"ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset", 
//         Access-Control-Allow-Origin:"*", Strict-Transport-Security:"max-age=31536000; includeSubdomains; preload", X-Frame-Options:"deny", 
//         X-Content-Type-Options:"nosniff", X-XSS-Protection:"0", Referrer-Policy:"origin-when-cross-origin, strict-origin-when-cross-origin", 
//         Content-Security-Policy:"default-src 'none'", Vary:"Accept-Encoding, Accept, X-Requested-With", X-GitHub-Request-Id:"C992:507A:98EAD0:9C814F:62672C67"])
// """`
//             [Server:\"GitHub.com\", Date:\"Mon, 25 Apr 2022 23:19:04 GMT\", Content-Type:\"application/json; charset=utf-8\", 
//             Transfer-Encoding:\"chunked\", X-OAuth-Scopes:\"admin:org, public_repo, read:user, user:email\", X-Accepted-OAuth-Scopes:\"repo\", 
//             x-oauth-client-id:\"1498bd829e8d1195c8d7\", X-GitHub-Media-Type:\"github.v4\", X-RateLimit-Limit:\"5000\", X-RateLimit-Remaining:\"4971\", 
//             X-RateLimit-Reset:\"1650929778\", X-RateLimit-Used:\"29\", X-RateLimit-Resource:\"graphql\", 
//             Access-Control-Expose-Headers:\"ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset\", 
//             Access-Control-Allow-Origin:\"*\", Strict-Transport-Security:\"max-age=31536000; includeSubdomains; preload\", X-Frame-Options:\"deny\", 
//             X-Content-Type-Options:\"nosniff\", X-XSS-Protection:\"0\", Referrer-Policy:\"origin-when-cross-origin, strict-origin-when-cross-origin\", 
//             Content-Security-Policy:\"default-src 'none'\", Vary:\"Accept-Encoding, Accept, X-Requested-With\", X-GitHub-Request-Id:\"C992:507A:98EAD0:9C814F:62672C67\"]
//         """

  @Test
  void test_projectId_returns_id() {
    GithubService githubService = mock(GithubService.class);
    GithubGraphQLService mockGithubApi = mock(GithubGraphQLService.class);

    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    headers.put("Server", new ArrayList<String>(List.of("GitHub.com")));
    headers.put("Date", new ArrayList<String>(List.of("Mon, 25 Apr 2022 23:19:04 GMT")));
    headers.put("Content-Type", new ArrayList<String>(List.of("application/json; charset=utf-8")));
    headers.put("Transfer-Encoding", new ArrayList<String>(List.of("chunked")));
    headers.put("X-OAuth-Scopes", new ArrayList<String>(List.of("admin:org, public_repo, read:user, user:email")));
    headers.put("X-Accepted-OAuth-Scopes", new ArrayList<String>(List.of("repo")));
    headers.put("x-oauth-client-id", new ArrayList<String>(List.of("1498bd829e8d1195c8d7")));
    headers.put("X-GitHub-Media-Type", new ArrayList<String>(List.of("github.v4")));
    headers.put("X-RateLimit-Limit", new ArrayList<String>(List.of("5000")));
    headers.put("X-RateLimit-Remaining", new ArrayList<String>(List.of("4971")));
    headers.put("X-RateLimit-Reset", new ArrayList<String>(List.of("1650929778")));
    headers.put("X-RateLimit-Used", new ArrayList<String>(List.of("29")));
    headers.put("X-RateLimit-Resource", new ArrayList<String>(List.of("graphql")));
    headers.put("Access-Control-Expose-Headers", new ArrayList<String>(List.of("ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset")));
    headers.put("Access-Control-Allow-Origin", new ArrayList<String>(List.of("*")));
    headers.put("Strict-Transport-Security", new ArrayList<String>(List.of("max-age=31536000; includeSubdomains; preload")));
    headers.put("X-Frame-Options", new ArrayList<String>(List.of("deny")));
    headers.put("X-Content-Type-Options", new ArrayList<String>(List.of("nosniff")));
    headers.put("X-XSS-Protection", new ArrayList<String>(List.of("0")));
    headers.put("Referrer-Policy", new ArrayList<String>(List.of("origin-when-cross-origin, strict-origin-when-cross-origin")));
    headers.put("Content-Security-Policy", new ArrayList<String>(List.of("default-src 'none'")));
    headers.put("Vary", new ArrayList<String>(List.of("Accept-Encoding, Accept, X-Requested-With")));
    headers.put("X-GitHub-Request-Id", new ArrayList<String>(List.of("C992:507A:98EAD0:9C814F:62672C67")));

    List<Map.Entry<String, String>> headers2 = new ArrayList<Map.Entry<String, String>>();
    headers2.add(Map.entry("Server", "GitHub.com"));
    headers2.add(Map.entry("Date", "Mon, 25 Apr 2022 23:19:04 GMT"));
    // headers.put("Content-Type", new ArrayList<String>(List.of("application/json; charset=utf-8")));
    // headers.put("Transfer-Encoding", new ArrayList<String>(List.of("chunked")));
    // headers.put("X-OAuth-Scopes", new ArrayList<String>(List.of("admin:org, public_repo, read:user, user:email")));
    // headers.put("X-Accepted-OAuth-Scopes", new ArrayList<String>(List.of("repo")));
    // headers.put("x-oauth-client-id", new ArrayList<String>(List.of("1498bd829e8d1195c8d7")));
    // headers.put("X-GitHub-Media-Type", new ArrayList<String>(List.of("github.v4")));
    // headers.put("X-RateLimit-Limit", new ArrayList<String>(List.of("5000")));
    // headers.put("X-RateLimit-Remaining", new ArrayList<String>(List.of("4971")));
    // headers.put("X-RateLimit-Reset", new ArrayList<String>(List.of("1650929778")));
    // headers.put("X-RateLimit-Used", new ArrayList<String>(List.of("29")));
    // headers.put("X-RateLimit-Resource", new ArrayList<String>(List.of("graphql")));
    // headers.put("Access-Control-Expose-Headers", new ArrayList<String>(List.of("ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset")));
    // headers.put("Access-Control-Allow-Origin", new ArrayList<String>(List.of("*")));
    // headers.put("Strict-Transport-Security", new ArrayList<String>(List.of("max-age=31536000; includeSubdomains; preload")));
    // headers.put("X-Frame-Options", new ArrayList<String>(List.of("deny")));
    // headers.put("X-Content-Type-Options", new ArrayList<String>(List.of("nosniff")));
    // headers.put("X-XSS-Protection", new ArrayList<String>(List.of("0")));
    // headers.put("Referrer-Policy", new ArrayList<String>(List.of("origin-when-cross-origin, strict-origin-when-cross-origin")));
    // headers.put("Content-Security-Policy", new ArrayList<String>(List.of("default-src 'none'")));
    // headers.put("Vary", new ArrayList<String>(List.of("Accept-Encoding, Accept, X-Requested-With")));
    // headers.put("X-GitHub-Request-Id", new ArrayList<String>(List.of("C992:507A:98EAD0:9C814F:62672C67")));

    GraphQLResponse gqlResponse = new GraphQLResponse("""
        {
            \"data\" : {
                \"repository\" : {
                    \"project\" : {
                        \"id\" : \"PRO_kwLOG0U47s4A11-W\",
                        \"name\" : \"W22 Play Page\"
                    }
                }
            }
        }
        """, 
        headers
    );

    when(mockGithubApi.executeGraphQLQuery("""
        query($owner: String!, $repo: String!, $projNum: Int!){
            repository(owner: $owner, name: $repo) {
                project(number: $projNum) {
                id
                name
                }
            }
            }
        """,
          Map.of(
            "owner", "ucsb-cs156-w22",
            "repo", "HappierCows",
            "projNum", 1
          ))
        ).thenReturn(gqlResponse);

    System.out.println(gqlResponse);
    // System.out.println(headers2);
    assertEquals("PRO_kwLOG0U47s4A11-W", githubService.projectId("ucsb-cs156-w22", "HappierCows", 1));
  }

  // @Test
  // void test_projectId_returns_empty() {

  //   GithubGraphQLService githubGraphQLService = mock(GithubGraphQLService.class);
  //   GraphQLResponse gqlResponse = new GraphQLResponse("""
  //       {
  //           "repository" : {
  //               "project" : {
  //                   "id" : "",
  //                   "name" : ""
  //               }
  //           }
  //       }
  //       """);
  //   when(executeGraphQLQuery("""
  //       query($owner: String!, $repo: String!, $projNum: Int!){
  //           repository(owner: $owner, name: $repo) {
  //               project(number: $projNum) {
  //               id
  //               name
  //               }
  //           }
  //           }
  //         """,
  //         Map.of(
  //           "owner", "ucsb-cs156-w22",
  //           "repo", "HappierCows",
  //           "projNum", 1
  //         )))
  //       .thenReturn(gqlResponse);
  //   assertEquals(githubGraphQLService.projectId("ucsb-cs156-w22", "HappierCows", 1), "");
  // }

}
