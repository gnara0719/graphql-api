package graphql.reslover;

import graphql.entity.Author;
import graphql.entity.Book;
import graphql.service.AuthorService;
import graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL Query Resolver
 * 데이터 조회를 처리합니다.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class QueryResolver {

    private final BookService bookService;
    private final AuthorService authorService;

    /**
     * 모든 도서 조회
     *
     * @return List<Book>
     */
    @QueryMapping(name=  "books")
    public List<Book> getAllBooks() {
        log.info("GraphQL Query: books");
        return bookService.getAllBooksWithAuthor();
    }

    /**
     * ID로 도서 조회
     * @param id
     * @return Book
     */
    @QueryMapping
    public Book book(@Argument Long id) {
        log.info("GraphQL Query: book");
        return bookService.getBookById(id);
    }

    /**
     * 제목으로 도서 검색
     * @param title
     * @return List<Book>
     */
    @QueryMapping
    public List<Book> searchBooks(@Argument String title) {
        log.info("GraphQL Query: searchBooks(title={}", title);
        return bookService.searchBooks(title);
    }

    /**
     * 모든 저자 조회
     */
    public List<Author> authors() {
        log.info("GraphQL Query: authors");
        return authorService.getAllAuthors();
    }

    /**
     * ID로 저자 조회
     * @param id
     * @return Author
     */
    @QueryMapping
    public Author author(@Argument Long id) {
        log.info("GraphQL Query: author");
        return authorService.getAuthorById(id);
    }

    /**
     * 저자별 도서 조회
     * @param authorId
     * @return List<Book>
     */
    @QueryMapping
    public List<Book> booksByAuthor(@Argument Long authorId) {
        log.info("GraphQL Query: booksByAuthor(authorId={})", authorId);
        return bookService.getBooksByAuthor(authorId);
    }
}
