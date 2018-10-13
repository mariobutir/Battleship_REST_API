package mario.battleship.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BoardRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String row;
}
