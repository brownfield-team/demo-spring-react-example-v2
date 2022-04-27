package edu.ucsb.cs156.example.services;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class GraphQLPaginationService {

  private static class PaginationIterator implements Iterator<GraphQLResponse> {
    private Function<String, GraphQLResponse> query;
    private Predicate<GraphQLResponse> paginationCondition;
    private Function<GraphQLResponse, String> cursorFunction;
    private String cursor;

    private GraphQLResponse lastResponse;

    private PaginationIterator(Function<String, GraphQLResponse> query,
                               Predicate<GraphQLResponse> paginationCondition,
                               Function<GraphQLResponse, String> cursorFunction,
                               String cursor) {
      this.query = query;
      this.paginationCondition = paginationCondition;
      this.cursorFunction = cursorFunction;
      this.cursor = cursor;
    }

    @Override
    public boolean hasNext() {
      return lastResponse == null ? true : paginationCondition.test(lastResponse);
    }

    @Override
    public GraphQLResponse next() {
      lastResponse = query.apply(cursor);
      cursor = cursorFunction.apply(lastResponse);
      return lastResponse;
    }
  }

  /**
   * Iterates over a paginated GraphQL source, generating a stream of {@link GraphQLResponse} objects for each page.
   * <br>
   * This is the same as calling {@link #streamPaginatedQuery(Function, Predicate, Function, String)} using {@literal null} as the
   * initial cursor.
   *
   * @param queryFunction a function that executes a graphql query given a cursor
   * @param paginationCondition a function that determines whether there are more pages given a {@link GraphQLResponse}
   * @param cursorFunction a function that determines the cursor of the next page given a {@link GraphQLResponse}
   * @return a stream of GraphQL responses iterating over the paginated source
   * @see #streamPaginatedQuery(Function, Predicate, Function, String)
   */
  public Stream<GraphQLResponse> streamPaginatedQuery(Function<String, GraphQLResponse> queryFunction,
                                                      Predicate<GraphQLResponse> paginationCondition,
                                                      Function<GraphQLResponse, String> cursorFunction) {
    return streamPaginatedQuery(queryFunction, paginationCondition, cursorFunction, null);
  }

  /**
   * Iterates over a paginated GraphQL source, generating a stream of {@link GraphQLResponse} objects for each page.
   *
   * @param queryFunction a function that executes a graphql query given a cursor
   * @param paginationCondition a function that determines whether there are more pages given a {@link GraphQLResponse}
   * @param cursorFunction a function that determines the cursor of the next page given a {@link GraphQLResponse}
   * @param initialCursor the initial value to use for the cursor
   * @return a stream of GraphQL responses iterating over the paginated source
   */
  public Stream<GraphQLResponse> streamPaginatedQuery(Function<String, GraphQLResponse> queryFunction,
                                                      Predicate<GraphQLResponse> paginationCondition,
                                                      Function<GraphQLResponse, String> cursorFunction,
                                                      String initialCursor) {
    PaginationIterator iter =
      new PaginationIterator(queryFunction, paginationCondition, cursorFunction, initialCursor);

    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false);
  }
}
