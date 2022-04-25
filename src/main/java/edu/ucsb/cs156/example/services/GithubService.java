package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import edu.ucsb.cs156.example.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import edu.ucsb.cs156.example.errors.GenericBackendException;

import com.netflix.graphql.dgs.client.GraphQLResponse;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Service
public class GithubService {
  @Autowired
  private GithubGraphQLService githubApi;

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
