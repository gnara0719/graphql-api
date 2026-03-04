package graphql.repository;

import graphql.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(Long authorId);

    Book findByIsbn(String isbn);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author")
    List<Book> findAllWithAuthor();
}
