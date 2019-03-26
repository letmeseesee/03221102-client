package server;

import facade.vo.Reponse.Reponse;
import facade.vo.User;
import facade.vo.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConsoleTable;

import java.util.Scanner;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author
 */
public class clientMainServer {
    final static Logger logger = LoggerFactory.getLogger(clientMainServer.class);
    private User loginUser;
    private Scanner in = new Scanner(System.in);
    public clientMainServer(){

    }

    public void mainServer(){
        //登陆或者注册
        logger.info("输入i注册新用户，输入l登陆");
        String operation = in.nextLine();
        if("i".equals(operation)) {
            Boolean result = register();
        }else if("l".equals(operation)) {
            Boolean result = login();
        }
        //启动主循环
        mainLoop();
    }

    private Boolean register(){
        logger.info("输入用户名");
        String username = in.nextLine();

        logger.info("输入用户地址");
        String userAddress = in.nextLine();

        logger.info("输入用户电话");
        String userPhone = in.nextLine();

        logger.info("输入用户密码");
        String userPassword = in.nextLine();

        loginUser.setUserName(username);
        loginUser.setUserPhone(userPhone);
        loginUser.setUserAddress(userAddress);
        loginUser.setUserPassword(userPassword);

        Request registerRequest = new Request();
        registerRequest.setUser(loginUser);
        registerRequest.setTarget("addUser");

        //生成一个User实体发给服务端
        Reponse reponse = ClientServer.request(registerRequest);
        if(reponse.getCode() == 0){
            logger.info("注册成功");
            //账户金额初始化为0
            loginUser.setMoney(0);
            loginUser.setId(reponse.getUser().getId());
        }else{
            logger.info("注册失败");
            System.exit(0);
        }
        //请求服务端
        return true;
    }

    private Boolean login(){
        Scanner in = new Scanner(System.in);
        logger.info("输入用户名");
        String username = in.nextLine();

        logger.info("输入用户密码");
        String userPassword = in.nextLine();

        loginUser.setUserPassword(userPassword);
        loginUser.setUserName(username);

        Request loginRequest = new Request();
        loginRequest.setUser(loginUser);
        loginRequest.setTarget("getUser");

        //生成一个实体传给服务端判定是否存在
        Reponse reponse = ClientServer.request(loginRequest);
        if(reponse.getCode() == 0){
            logger.info("登陆成功");
            //账户金额初始化为0
            loginUser.setMoney(reponse.getUser().getMoney());
            loginUser.setUserAddress(reponse.getUser().getUserAddress());
            loginUser.setUserPhone(reponse.getUser().getUserPhone());
            loginUser.setId(reponse.getUser().getId());
        }else{
            logger.info("注册失败");
            System.exit(0);
        }
        return true;
    }

    private void mainLoop(){
        //主功能 1.存取款 2.流水记录 3.账户详情
        logger.info("输入需要执行的操作");
        logger.info("a:存款 b:取款 c:查询余额 d:账户详情 e:退出");
        String operation = in.nextLine();
        Boolean result = true;
        while(true){
            switch (operation.charAt(0)){
                case 'a':
                    result = save();
                    break;
                case 'b':
                    result = take();
                    break;
                case 'c':
                    result = listFlow();
                    break;
                case 'd':
                    result = detail();
                    break;
                case 'e':
                    logger.info("客户端退出中。。。");
                    System.exit(0);
                    break;
                default:
                    logger.warn("非法操作");
            }
            logger.info("输入需要执行的操作");
            logger.info("a:存款 b:取款 c:查询余额 d:账户详情");
            operation = in.nextLine();
        }
    }

    /**
     * 存款
     * @return boolean
     */
    private Boolean save(){
        logger.info("输入存款金额：");
        Integer savingMoney = in.nextInt();
        loginUser.setMoney(loginUser.getMoney() + savingMoney);
        loginUser.setCharge(savingMoney);
        Request savingRequest = new Request();
        savingRequest.setChargeType(1);
        savingRequest.setUser(loginUser);
        savingRequest.setTarget("saveMoney");

        Reponse reponse = ClientServer.request(savingRequest);
        if(reponse.getCode() == 0){
            logger.info("存款成功");
            loginUser.setMoney(reponse.getUser().getMoney());
        }else{
            logger.info("存款失败");
        }
        return true;
    }

    /**
     * 取款
     * @return boolean
     */
    private Boolean take(){
        logger.info("输入取款金额：");
        Integer takeMoney = in.nextInt();
        if(takeMoney > loginUser.getMoney()){
            logger.warn("账户余额不足");
            return false;
        }
        loginUser.setMoney(loginUser.getMoney() - takeMoney);
        loginUser.setCharge(takeMoney);
        Request takeRequest = new Request();
        takeRequest.setChargeType(2);
        takeRequest.setUser(loginUser);
        takeRequest.setTarget("saveMoney");

        Reponse reponse = ClientServer.request(takeRequest);
        if(reponse.getCode() == 0){
            logger.info("取款成功");
            loginUser.setMoney(reponse.getUser().getMoney());
            loginUser.setCharge(reponse.getUser().getCharge());
        }else{
            logger.info("取款成功");
        }
        return true;
    }

    /**
     * 流水记录
     * @return boolean
     */
    private Boolean listFlow(){
        logger.info("尝试连接服务器获取记录。。。");
        Request savingRequest = new Request();
        savingRequest.setChargeType(0);
        savingRequest.setUser(loginUser);
        savingRequest.setTarget("saveMoney");
        return true;
    }

    /**
     * 账户详情
     * @return boolean
     */
    private Boolean detail(){
        ConsoleTable t = new ConsoleTable(4, true);
        t.appendRow();
        t.appendColum("序号").appendColum("姓名").appendColum("电话").appendColum("地址").appendColum("余额");

        t.appendRow();
        t.appendColum(loginUser.getId()).appendColum(loginUser.getUserName()).appendColum(loginUser.getUserAddress()).appendColum(loginUser.getMoney());

        System.out.println(t.toString());
        return true;
    }
}
