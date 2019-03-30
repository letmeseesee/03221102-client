package server;

import config.Config;
import facade.vo.Reponse.Reponse;
import facade.vo.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author 2019-3-24
 * 发送客户端请求
 */
public class ClientServer {
    public static final int port = 8888;
    public static String host;

    public ClientServer() {
        ClientServer.host = Config.serverHost;
    }

    public static String send(String requestString) {
        Socket socket = null;
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(host, port);
            //向服务器端发送数据
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(requestString);
//                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                System.out.println(str);

            //读取服务器端返回数据
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String ret = input.readLine();
            System.out.println("服务器端返回过来的是: " + ret);
            out.close();
            input.close();
            return ret;
        } catch (Exception e) {
            System.out.println("服务端异常:" + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
        return null;
    }

    static Reponse request(Request loginRequest) {
        System.out.println("请求服务端中。。。");
        String loginRequestSendToService = toJSONString(loginRequest);
        String resonseString = ClientServer.send(loginRequestSendToService);
        return parseObject(resonseString, Reponse.class);
    }
}
