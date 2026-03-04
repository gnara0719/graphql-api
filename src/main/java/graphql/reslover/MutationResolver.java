package graphql.reslover;

import graphql.entity.Author;
import graphql.entity.Book;
import graphql.input.CreateAuthorInput;
import graphql.input.CreateBookInput;
import graphql.input.UpdateAuthorInput;
import graphql.input.UpdateBookInput;
import graphql.service.AuthorService;
import graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MutationResolver {

    private final BookService bookService;
    private final AuthorService authorService;

    /**
     * 도서 생성
     */
    @MutationMapping
    public Book createBook(@Argument CreateBookInput input) {
        log.info("GraphQL Mutation: createBook");
        return bookService.createBook(input);
    }

    /**
     * 도서 수정
     */
    @MutationMapping
    public Book updateBook(@Argument Long id, @Argument UpdateBookInput input) {
        log.info("GraphQL Mutation: updateBook(id={}, {})", id, input);
        return bookService.updateBook(id, input);
    }

    /**
     * 도서 삭제
     */
    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        log.info("GraphQL Mutation: deleteBook(id={})", id);
        return bookService.deleteBook(id);
    }

    /**
     * 저자 생성
     */
    @MutationMapping
    public Author createAuthor(@Argument CreateAuthorInput input) {
        log.info("GraphQL Mutation: createAuthor({})", input);
        return authorService.createAuthor(input);
    }

    /**
     * 저자 수정
     */
    @MutationMapping
    public Author updateAuthor(@Argument Long id, @Argument UpdateAuthorInput input) {
        log.info("GraphQL Mutation: updateAuthor(id={}, {})", id, input);
        return authorService.updateAuthor(id, input);
    }

    /**
     * 저자 삭제
     */
    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id) {
        log.info("GraphQL Mutation: deleteAuthor(id={})", id);
        return authorService.deleteAuthor(id);
    }
}
