package mario.battleship.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class GameList {

    @Id
    private Long id;

    @OneToMany
    private List<GameListObject> list = new ArrayList<>();


}
