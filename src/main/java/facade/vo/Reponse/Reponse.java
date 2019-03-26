package facade.vo.Reponse;

import facade.vo.User;
import lombok.Data;

import java.util.List;

@Data
public class Reponse {
    Integer code;
    String msg;
    User user;
    List<User> users;
    List<User> userFlows;
}
