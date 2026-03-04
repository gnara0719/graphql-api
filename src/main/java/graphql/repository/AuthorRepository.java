package graphql.repository;

import graphql.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllWithBooks();

    Optional<Author> findByEmail(String email);
}
