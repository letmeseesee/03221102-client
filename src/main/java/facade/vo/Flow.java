package facade.vo;

import lombok.Data;

@Data
public class Flow {
    Integer id;
    Integer accountId;
    Integer type;
    Integer charge;
    Integer money;
}
