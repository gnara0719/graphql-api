package graphql.service;

import graphql.entity.Author;
import graphql.entity.Book;
import graphql.input.CreateBookInput;
import graphql.input.UpdateBookInput;
import graphql.repository.AuthorRepository;
import graphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * 모든 도서 조회
     */
    public List<Book> getAllBooks() {
        log.info("📚 모든 도서 조회");
        return bookRepository.findAll();
    }

    /**
     * 모든 도서 조회 (저자 정보 포함 - N+1 문제 해결)
     */
    public List<Book> getAllBooksWithAuthor() {
        log.info("📚 모든 도서 조회 (저자 포함)");
//        return bookRepository.findAllWithAuthor();
        return bookRepository.findAll();
    }


    /**
     * ID로 도서 조회
     */
    public Book getBookById(Long id) {
        log.info("📖 도서 조회: ID={}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다: " + id));
    }

    /**
     * 제목으로 도서 검색
     */
    public List<Book> searchBooks(String title) {
        log.info("🔍 도서 검색: title={}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * 저자별 도서 조회
     */
    public List<Book> getBooksByAuthor(Long authorId) {
        log.info("📚 저자별 도서 조회: authorId={}", authorId);
        return bookRepository.findByAuthorId(authorId);
    }

    /**
     * 도서 생성
     */
    @Transactional
    public Book createBook(CreateBookInput input) {
        log.info("➕ 도서 생성: {}", input);

        // 저자 확인
        Author author = authorRepository.findById(input.getAuthorId())
                .orElseThrow(() -> new RuntimeException("저자를 찾을 수 없습니다: " + input.getAuthorId()));

        // 도서 생성
        Book book = Book.builder()
                .title(input.getTitle())
                .isbn(input.getIsbn())
                .publishedYear(input.getPublishedYear())
                .price(input.getPrice())
                .author(author)
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("✅ 도서 생성 완료: {}", savedBook.getId());

        return savedBook;
    }

    /**
     * 도서 수정
     */
    @Transactional
    public Book updateBook(Long id, UpdateBookInput input) {
        log.info("✏️ 도서 수정: ID={}, input={}", id, input);

        Book book = getBookById(id);

        // 필드 업데이트
        if (input.getTitle() != null) {
            book.setTitle(input.getTitle());
        }
        if (input.getIsbn() != null) {
            book.setIsbn(input.getIsbn());
        }
        if (input.getPublishedYear() != null) {
            book.setPublishedYear(input.getPublishedYear());
        }
        if (input.getPrice() != null) {
            book.setPrice(input.getPrice());
        }
        if (input.getAuthorId() != null) {
            Author author = authorRepository.findById(input.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("저자를 찾을 수 없습니다: " + input.getAuthorId()));
            book.setAuthor(author);
        }

        log.info("✅ 도서 수정 완료: {}", id);
        return book;
    }

    /**
     * 도서 삭제
     */
    @Transactional
    public boolean deleteBook(Long id) {
        log.info("🗑️ 도서 삭제: ID={}", id);

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("도서를 찾을 수 없습니다: " + id);
        }

        bookRepository.deleteById(id);
        log.info("✅ 도서 삭제 완료: {}", id);

        return true;
    }
}
