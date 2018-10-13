package mario.battleship.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlayerListDTO {

    @JsonProperty("players")
    private List<PlayerListObjectDTO> playerDTOList;
}
