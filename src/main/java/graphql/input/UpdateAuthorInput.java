package graphql.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateAuthorInput {

    private String name;
    private String email;
}
