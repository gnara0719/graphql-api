package graphql.config;

import graphql.entity.Author;
import graphql.entity.Book;
import graphql.repository.AuthorRepository;
import graphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        log.info("초기 데이터 생성 시작");

        // 저자 생성
        Author author1 = Author.builder()
                .name("김영한")
                .email("younghank@example.com")
                .build();

        Author author2 = Author.builder()
                .name("로버트 마틴")
                .email("unclebob@example.com")
                .build();

        authorRepository.save(author1);
        authorRepository.save(author2);

        // 도서 생성
        Book book1 = Book.builder()
                .title("스프링 부트와 JPA 활용")
                .isbn("978-1234567890")
                .publishedYear(2023)
                .price(35000.0)
                .author(author1)
                .build();

        Book book2 = Book.builder()
                .title("Clean Code")
                .isbn("978-0132350884")
                .publishedYear(2008)
                .price(42000.0)
                .author(author2)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        log.info("초기 데이터 생성 완료!");
    }
}
