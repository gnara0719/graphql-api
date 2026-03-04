package graphql.exception;

import org.springframework.graphql.execution.ErrorType;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;

import java.util.Map;

public class GraphQLExceptionHandler {

    // GraphQL의 예외처리는 리턴 타입을 GraphQLError로 세팅하고
    // 추가 정보가 필요하다면 extensions에 Map으로 전달
    @GraphQlExceptionHandler
    public GraphQLError handleNotFoundException(BookNotFoundException e) {
        return GraphQLError.newError()
                .message(e.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .extensions(Map.of(
                        "errorCode", "ERR-BOOK-001",
                        "httpStatus", "404"
                ))
                .build();
    }
}
