package graphql.reslover;

import graphql.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Controller
public class BookSubscriptionResolver {

    // Sink: 이벤트를 발행하는 publisher (방송국)
    private final Sinks.Many<Book> bookSink = Sinks.many().multicast().onBackpressureBuffer();

    /**
     * 새 도서 추가 시 실시간 알림 (방송국에 주파수 맞추기)
     *
     * @return Flux<Book>
     */
    @SubscriptionMapping
    public Flux<Book> bookAdded() {
        log.info("GraphQL Subscription: bookAdded");
        return bookSink.asFlux();
    }

    /**
     * 도서 추가 이벤트 발행 (주파수 맞춘 애들에게 방송 전파!)
     */
    public void publishBookAdded(Book book) {
        log.info("도서 추가 이벤트 발행: {}", book.getTitle());
        bookSink.tryEmitNext(book);
    }

}
