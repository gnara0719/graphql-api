package graphql.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateBookInput {

    private String title;
    private String isbn;
    private Integer publishedYear;
    private Double price;
    private Long authorId;
}
