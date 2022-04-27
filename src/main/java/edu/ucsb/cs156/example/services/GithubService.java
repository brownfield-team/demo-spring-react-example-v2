package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import edu.ucsb.cs156.example.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import edu.ucsb.cs156.example.errors.GenericBackendException;

import java.util.List;
import java.util.Map;

@Service
public class GithubService {
  @Autowired
  private GithubGraphQLService githubApi;

  @Autowired
  private GraphQLPaginationService paginationService;

  public String createProject(String ownerId, String name) {
    GraphQLResponse response = githubApi.executeGraphQLQuery("""
        mutation($ownerId: ID!, $name: String!) {
          createProject(input: {ownerId: $ownerId, name: $name}) {
            project {
              id
            }
          }
        }
        """,
      Map.of(
        "ownerId", ownerId,
        "name", name
      ));

    return response.extractValue("createProject.project.id");
  }

  public String addColumnToProject(String projectId, String columnName) {
    GraphQLResponse response = githubApi.executeGraphQLQuery("""
        mutation($projectId: ID!, $name: String!) {
          addProjectColumn(input: {projectId: $projectId, name: $name}) {
            columnEdge {
              node {
                id
              }
            }
          }
        }
        """,
      Map.of(
        "projectId", projectId,
        "name", columnName
      ));

    return response.extractValue("addProjectColumn.columnEdge.node.id");
  }

  public String createIssue(String repoId, Issue issue) {
    GraphQLResponse response = githubApi.executeGraphQLQuery("""
        mutation($repoId: ID!, $title: String!, $body: String!) {
          createIssue(input: {repositoryId: $repoId, title: $title, body: $body}) {
            issue {
              id
            }
          }
        }
        """,
      Map.of(
        "repoId", repoId,
        "title", issue.getTitle(),
        "body", issue.getBody()
      ));

    return response.extractValue("createIssue.issue.id");
  }

  public String addIssueToProjectColumn(String columnId, String issueOrPullRequestId) {
    GraphQLResponse response = githubApi.executeGraphQLQuery("""
        mutation($columnId: ID!, $contentId: ID!) {
          addProjectCard(input: {projectColumnId: $columnId, contentId: $contentId}) {
            cardEdge {
              node {
                id
              }
            }
          }
        }
        """,
      Map.of(
        "columnId", columnId,
        "contentId", issueOrPullRequestId
      ));

    return response.extractValue("addProjectCard.cardEdge.node.id");
  }

  public String getProjectName(String projectId) {
    GraphQLResponse response = githubApi.executeGraphQLQuery("""
        query($projectId: ID!) {
          node(id: $projectId) {
            ... on Project {
              name
            }
          }
        }
        """,
      Map.of(
        "projectId", projectId
      ));

    return response.extractValue("node.name");
  }

  private GraphQLResponse fetchIssuesFromColumn(String columnId, String cursor) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("columnId", columnId);

    if (cursor != null) {
      variables.put("cursor", cursor);
    }

    return githubApi.executeGraphQLQuery("""
        query($columnId: ID!, $cursor: String) {
          node(id: $columnId) {
            ... on ProjectColumn {
              cards(first: 100, after: $cursor) {
                pageInfo {
                  hasNextPage
                  endCursor
                }
                edges {
                  node {
                    content {
                      __typename
                      ... on Issue {
                        title
                        body
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """,
      variables
    );
  }

  private List<Issue> getIssuesFromColumn(String columnId) {
    return paginationService.streamPaginatedQuery(
        cursor -> fetchIssuesFromColumn(columnId, cursor),
        r -> r.extractValue("node.cards.pageInfo.hasNextPage"),
        r -> r.extractValue("node.cards.pageInfo.endCursor")
      )
      .flatMap(r ->
        r.<List<Map<String, String>>>extractValue("node.cards.edges[*].node.content")
          .stream()
          .filter(Objects::nonNull)
      )
      .filter(m -> m.get("__typename").equals("Issue"))
      .map(data ->
        Issue.builder()
          .title(data.get("title"))
          .body(data.get("body"))
          .build()
      )
      .toList();
  }

  private GraphQLResponse queryProjectColumnsAndIssues(String projectId, String cursor) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("projectId", projectId);

    if (cursor != null) {
      variables.put("cursor", cursor);
    }

    return githubApi.executeGraphQLQuery("""
        query($projectId: ID!, $cursor: String) {
          node(id: $projectId) {
            ... on Project {
              columns(first: 100, after: $cursor) {
                pageInfo {
                  hasNextPage
                  endCursor
                }
                edges {
                  node {
                    id
                    name
                  }
                }
              }
            }
          }
        }
        """,
      variables
    );
  }

  public Map<String, List<Issue>> getProjectColumnsAndIssues(String projectId) {
    return paginationService.streamPaginatedQuery(
        cursor -> queryProjectColumnsAndIssues(projectId, cursor),
        r -> r.extractValue("node.columns.pageInfo.hasNextPage"),
        r -> r.extractValue("node.columns.pageInfo.endCursor")
      )
      .flatMap(r -> r.<List<Map<String, String>>>extractValue("node.columns.edges[*].node").stream())
      .collect(
        // Using a LinkedHashMap rather than Collectors.toMap to preserve insertion order
        LinkedHashMap::new,
        (map, column) -> map.put(column.get("name"), getIssuesFromColumn(column.get("id"))),
        Map::putAll
      );
  }

  public String repositoryId(String owner, String repo) {
    GraphQLResponse result = githubApi.executeGraphQLQuery("""
      query($owner: String!, $repo: String!){
        repository(owner: $owner, name: $repo) {
            id
            name
        }
        }
      """,
      Map.of(
        "owner", owner,
        "repo", repo
      ));

    return result.extractValue("repository.id");
  }

  public String projectId(String owner, String repo, int projNum) {
      GraphQLResponse result = githubApi.executeGraphQLQuery("""
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
          "owner", owner,
          "repo", repo,
          "projNum", projNum
        ));

      if(result.extractValue("repository.project") == null){
        throw new GenericBackendException("No project with number %d in %s/%s".formatted(projNum, owner, repo));
      }
      return result.extractValue("repository.project.id");
  }
}
