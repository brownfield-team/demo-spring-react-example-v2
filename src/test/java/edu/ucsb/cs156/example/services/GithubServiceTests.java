package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import edu.ucsb.cs156.example.GraphQLTestUtils;
import edu.ucsb.cs156.example.models.Issue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GithubService.class, GraphQLPaginationService.class})
public class GithubServiceTests {

  @MockBean
  private GithubGraphQLService gql;

  @Autowired
  private GithubService github;

  @Test
  public void createProject__valid() {
    ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Map<String, Object>> variables = ArgumentCaptor.forClass(Map.class);

    GraphQLResponse response = GraphQLTestUtils.newResponse(
      Map.of(
        "data", Map.of(
          "createProject", Map.of(
            "project", Map.of(
              "id", "PRO_dummyid"
            )
          )
        )
      )
    );

    when(gql.executeGraphQLQuery(query.capture(), variables.capture()))
      .thenReturn(response);

    String id = github.createProject("R_dummyid", "New project");

    GraphQLTestUtils.verifyCapturedGraphQLQueries(query, variables);

    assertThat(id).isEqualTo("PRO_dummyid");
    assertThat(variables.getValue())
      .containsExactlyEntriesOf(Map.of(
        "ownerId", "R_dummyid",
        "name", "New project"
      ));
  }

  @Test
  public void addColumnToProject__valid() {
    ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Map<String, Object>> variables = ArgumentCaptor.forClass(Map.class);

    GraphQLResponse response = GraphQLTestUtils.newResponse(
      Map.of(
        "data", Map.of(
          "addProjectColumn", Map.of(
            "columnEdge", Map.of(
              "node", Map.of(
                "id", "COL_dummyid"
              )
            )
          )
        )
      )
    );

    when(gql.executeGraphQLQuery(query.capture(), variables.capture()))
      .thenReturn(response);

    String id = github.addColumnToProject("PRO_dummyid", "New column");

    GraphQLTestUtils.verifyCapturedGraphQLQueries(query, variables);

    assertThat(id).isEqualTo("COL_dummyid");
    assertThat(variables.getValue())
      .containsExactlyEntriesOf(Map.of(
        "projectId", "PRO_dummyid",
        "name", "New column"
      ));
  }

  @Test
  public void createIssue__valid() {
    ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Map<String, Object>> variables = ArgumentCaptor.forClass(Map.class);

    GraphQLResponse response = GraphQLTestUtils.newResponse(
      Map.of(
        "data", Map.of(
          "createIssue", Map.of(
            "issue", Map.of(
              "id", "ISSUE_dummyid"
            )
          )
        )
      )
    );

    when(gql.executeGraphQLQuery(query.capture(), variables.capture()))
      .thenReturn(response);

    Issue issue = Issue.builder()
      .title("Issue title")
      .body("Issue body")
      .build();

    String id = github.createIssue("R_dummyid", issue);

    GraphQLTestUtils.verifyCapturedGraphQLQueries(query, variables);

    assertThat(id).isEqualTo("ISSUE_dummyid");
    assertThat(variables.getValue())
      .containsExactlyEntriesOf(Map.of(
        "repoId", "R_dummyid",
        "title", "Issue title",
        "body", "Issue body"
      ));
  }

  @Test
  public void addIssueToProjectColumns__valid() {
    ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Map<String, Object>> variables = ArgumentCaptor.forClass(Map.class);

    GraphQLResponse response = GraphQLTestUtils.newResponse(
      Map.of(
        "data", Map.of(
          "addProjectCard", Map.of(
            "cardEdge", Map.of(
              "node", Map.of(
                "id", "CARD_dummyid"
              )
            )
          )
        )
      )
    );

    when(gql.executeGraphQLQuery(query.capture(), variables.capture()))
      .thenReturn(response);

    String id = github.addIssueToProjectColumn("COL_dummyid", "ISSUE_dummyid");

    GraphQLTestUtils.verifyCapturedGraphQLQueries(query, variables);

    assertThat(id).isEqualTo("CARD_dummyid");
    assertThat(variables.getValue())
      .containsExactlyEntriesOf(Map.of(
        "columnId", "COL_dummyid",
        "contentId", "ISSUE_dummyid"
      ));
  }

  @Test
  public void getProjectName__valid() {
    ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Map<String, Object>> variables = ArgumentCaptor.forClass(Map.class);

    GraphQLResponse response = GraphQLTestUtils.newResponse(
      Map.of(
        "data", Map.of(
          "node", Map.of(
            "name", "Project name"
          )
        )
      )
    );

    when(gql.executeGraphQLQuery(query.capture(), variables.capture()))
      .thenReturn(response);

    String name = github.getProjectName("PRO_dummyid");

    GraphQLTestUtils.verifyCapturedGraphQLQueries(query, variables);

    assertThat(name).isEqualTo("Project name");
    assertThat(variables.getValue())
      .containsExactlyEntriesOf(Map.of(
        "projectId", "PRO_dummyid"
      ));
  }
}
