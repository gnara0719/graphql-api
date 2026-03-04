package graphql.reslover;

import graphql.entity.Author;
import graphql.entity.Book;
import graphql.service.AuthorService;
import graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @BatchMapping(typeName = "Book", field = "author")
    public Map<Book, Author> authors(List<Book> books) {
        log.info("도서 {}권에 대한 작가 정보를 일괄 조회", books.size());

        // 1. 여러 권의 책에서 작가 ID만 뽑아서 중복없는 리스트로 만듬
        List<Long> authorIds = books.stream()
                .map(book -> book.getAuthor().getId())
                .distinct()
                .toList();

        // 2. 작가 아이디 목록을 넘겨서 모든 작가를 한번에 찾아오기
        List<Author> authors = authorService.findAllById(authorIds);

        // 3. 가져온 작가를 책과 짝지어서 넘겨줌
//        return books.stream()
//                .collect(Collectors.toMap(
//                        book -> book,
//                        book -> authors.stream()
//                                .filter(author -> author.getId().equals(book.getAuthor().getId()))
//                                .findFirst()
//                                .orElse(null)
//                ));
        Map<Long, Author> authorMap = authors.stream()
                .collect(Collectors.toMap(Author::getId, author -> author));

        return books.stream()
                .collect(Collectors.toMap(
                        book -> book,
                        book -> authorMap.get(book.getAuthor().getId())
                ));
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
