package graphql.service;

import graphql.entity.Author;
import graphql.input.UpdateAuthorInput;
import graphql.repository.AuthorRepository;
import graphql.input.CreateAuthorInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * 모든 저자 조회
     */
    public List<Author> getAllAuthors() {
        log.info("👥 모든 저자 조회");
        return authorRepository.findAll();
    }

    /**
     * 모든 저자 조회 (도서 정보 포함 - N+1 문제 해결)
     */
    public List<Author> getAllAuthorsWithBooks() {
        log.info("👥 모든 저자 조회 (도서 포함)");
        return authorRepository.findAllWithBooks();
    }

    /**
     * ID로 저자 조회
     */
    public Author getAuthorById(Long id) {
        log.info("👤 저자 조회: ID={}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("저자를 찾을 수 없습니다: " + id));
    }

    /**
     * 저자 생성
     */
    @Transactional
    public Author createAuthor(CreateAuthorInput input) {
        log.info("➕ 저자 생성: {}", input);

        // 이메일 중복 확인
        if (authorRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + input.getEmail());
        }

        Author author = Author.builder()
                .name(input.getName())
                .email(input.getEmail())
                .build();

        Author savedAuthor = authorRepository.save(author);
        log.info("✅ 저자 생성 완료: {}", savedAuthor.getId());

        return savedAuthor;
    }

    /**
     * 저자 수정
     */
    @Transactional
    public Author updateAuthor(Long id, UpdateAuthorInput input) {
        log.info("✏️ 저자 수정: ID={}, input={}", id, input);

        Author author = getAuthorById(id);

        // 필드 업데이트
        if (input.getName() != null) {
            author.setName(input.getName());
        }
        if (input.getEmail() != null) {
            // 이메일 중복 확인 (자신 제외)
            authorRepository.findByEmail(input.getEmail())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new RuntimeException("이미 존재하는 이메일입니다: " + input.getEmail());
                        }
                    });
            author.setEmail(input.getEmail());
        }

        log.info("✅ 저자 수정 완료: {}", id);
        return author;
    }

    /**
     * 저자 삭제
     */
    @Transactional
    public boolean deleteAuthor(Long id) {
        log.info("🗑️ 저자 삭제: ID={}", id);

        Author author = getAuthorById(id);

        // 도서가 있는 저자는 삭제 불가
        if (!author.getBooks().isEmpty()) {
            throw new RuntimeException("도서가 있는 저자는 삭제할 수 없습니다");
        }

        authorRepository.deleteById(id);
        log.info("✅ 저자 삭제 완료: {}", id);

        return true;
    }

    public List<Author> findAllById(List<Long> authorIds) {
        return authorRepository.findAllById(authorIds);
    }
}
