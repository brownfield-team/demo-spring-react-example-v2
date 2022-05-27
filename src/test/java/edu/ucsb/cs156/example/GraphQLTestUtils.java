package edu.ucsb.cs156.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import graphql.language.Document;
import graphql.language.NonNullType;
import graphql.language.OperationDefinition;
import graphql.language.VariableDefinition;
import graphql.parser.InvalidSyntaxException;
import graphql.parser.Parser;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class GraphQLTestUtils {
  public static GraphQLResponse newResponse(Map<String, Object> structure) {
    try {
      String json = new ObjectMapper().writeValueAsString(structure);
      return new GraphQLResponse(json);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void verifyCapturedGraphQLQueries(ArgumentCaptor<String> queryCaptor,
                                                  ArgumentCaptor<Map<String, Object>> variablesCaptor) {

    List<String> queryCaptures = queryCaptor.getAllValues();
    List<Map<String, Object>> variableCaptures = variablesCaptor.getAllValues();

    assertThat(queryCaptures.size()).isEqualTo(variableCaptures.size())
      .withFailMessage(
        "Mismatch between captured queries and variables. There were %d captured queries and %d captured variables.",
        queryCaptures.size(),
        variableCaptures.size()
      );

    for (int i = 0; i < queryCaptures.size(); i++) {
      String query = queryCaptures.get(i);
      Map<String, Object> passedVariables = variableCaptures.get(i);
      Set<String> passedVariableNames = passedVariables.keySet();

      try {
        Document parsedQuery = Parser.parse(query);
        parsedQuery.getFirstDefinitionOfType(OperationDefinition.class)
          .ifPresent(op -> {
            Set<String> allQueryVariables =
              op.getVariableDefinitions().stream()
                .map(VariableDefinition::getName)
                .collect(Collectors.toSet());

            assertThat(passedVariables)
              .withFailMessage("""
                Variables passed by the client do not correspond to variables accepted by the GraphQL query.

                Variables allowed by the query are: %s
                Variables passed by client were:    %s
                """,
                allQueryVariables.stream().sorted().collect(Collectors.joining(", ")),
                passedVariableNames.stream().sorted().collect(Collectors.joining(", "))
              )
              .containsOnlyKeys(allQueryVariables);

            Set<String> requiredQueryVariables =
              op.getVariableDefinitions().stream()
                .filter(v -> v.getType() instanceof NonNullType)
                .map(VariableDefinition::getName)
                .collect(Collectors.toSet());

            assertThat(passedVariableNames)
              .withFailMessage("""
                Variables required by the GraphQL query were not provided by the client.

                Variables required in query are: %s
                Variables passed by client were: %s
                """,
                requiredQueryVariables.stream().sorted().collect(Collectors.joining(", ")),
                passedVariableNames.stream().sorted().collect(Collectors.joining(", "))
              )
              .containsAll(requiredQueryVariables);
          });
        parsedQuery.getChildren();

      } catch (InvalidSyntaxException e) {
        fail("%s\nPreview of malformed query:\n%s".formatted(e.getMessage(), e.getSourcePreview()));
      }
    }
  }
}
