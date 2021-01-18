package come.webserver.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private ServerSocket serverSocket;
    public WebServer(){
        System.out.println("正在启动客户端......");
        try {
            serverSocket = new ServerSocket(8088);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("客户端启动成功!");
    }
    public void start(){
        while(true){
            System.out.println("一个客户端正在连接.....");
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread t = new Thread(clientHandler);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("一个客户端已连接!");
        }
}
    public static void main(String[] args) {
        WebServer webServer =new WebServer();
        webServer.start();

    }
}
