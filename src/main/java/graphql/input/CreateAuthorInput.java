package graphql.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateAuthorInput {

    private String name;
    private String email;
}
