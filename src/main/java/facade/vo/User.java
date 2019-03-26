package facade.vo;

import lombok.Data;

/**
 *  @author 阿尔卑斯狗 2019-3-22 用户实体
 */
@Data
public class User {
    private Integer id;
    private String userName;
    private String userPassword;
    private String userAddress;
    private String userPhone;
    private Integer isAdmin;
    private Integer money;
    private Integer charge;
}
