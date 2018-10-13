package mario.battleship.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("player_id")
    private Long id;

    @Email
    public String email;

    public String name;

    @OneToOne
    private GameList gameList;

}
