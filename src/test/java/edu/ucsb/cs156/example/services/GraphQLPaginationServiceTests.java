package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GraphQLPaginationService.class})
public class GraphQLPaginationServiceTests {

  @Autowired
  private GraphQLPaginationService paginationService;

  @Test
  public void streamPaginatedQuery_withInitialCursor() {
    Function<String, GraphQLResponse> queryFunction = mock(Function.class);
    Predicate<GraphQLResponse> paginationFunction = mock(Predicate.class);
    Function<GraphQLResponse, String> cursorFunction = mock(Function.class);

    GraphQLResponse firstResponse = new GraphQLResponse("{ \"dummy\": 1 }");
    GraphQLResponse secondResponse = new GraphQLResponse("{ \"dummy\": 2 }");
    GraphQLResponse thirdResponse = new GraphQLResponse("{ \"dummy\": 3 }");

    when(queryFunction.apply("first cursor")).thenReturn(firstResponse);
    when(queryFunction.apply("second cursor")).thenReturn(secondResponse);
    when(queryFunction.apply("third cursor")).thenReturn(thirdResponse);

    when(paginationFunction.test(any()))
      .thenAnswer(invocation -> {
        GraphQLResponse arg = invocation.getArgument(0);
        return arg.equals(firstResponse) || arg.equals(secondResponse);
      });

    when(cursorFunction.apply(firstResponse)).thenReturn("second cursor");
    when(cursorFunction.apply(secondResponse)).thenReturn("third cursor");
    when(cursorFunction.apply(thirdResponse)).thenReturn("fourth cursor");

    List<GraphQLResponse> responses = paginationService.streamPaginatedQuery(
      queryFunction,
      paginationFunction,
      cursorFunction,
      "first cursor"
    ).toList();

    InOrder inOrder = inOrder(queryFunction);

    inOrder.verify(queryFunction).apply("first cursor");
    inOrder.verify(queryFunction).apply("second cursor");
    inOrder.verify(queryFunction).apply("third cursor");
    inOrder.verifyNoMoreInteractions();

    assertIterableEquals(List.of(firstResponse, secondResponse, thirdResponse), responses);
  }
}
