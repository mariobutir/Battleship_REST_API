package mario.battleship.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Conflict {

    private String error_code;
    private String error_arg;
}
