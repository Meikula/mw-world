package come.webserver.core;

import come.webserver.http.EmptyRequestException;
import come.webserver.http.HttpContext;
import come.webserver.http.HttpRequest;
import come.webserver.http.HttpResponse;
import come.webserver.servlet.LoginServlet;
import come.webserver.servlet.RegServlet;
import come.webserver.servlet.ShowAllUserServlet;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        try {
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            String path = request.getRequestURI();
            File file = new File("./webapps"+path);
            if("/myweb/regUser".equals(path)){
                RegServlet regServlet = new RegServlet();
                regServlet.service(request,response);

            }else if("/myweb/loginUser".equals(path)){
                LoginServlet loginServlet = new LoginServlet();
                loginServlet.service(request,response);
            }else if("/myweb/showAllUser".equals(path)) {
                ShowAllUserServlet showAllUserServlet = new ShowAllUserServlet();
                showAllUserServlet.service(request, response);
            }
            else{
                if(file.exists()&&file.isFile()){
                    response.setsFile(file);
                }else{
                    File fourFile = new File("./webapps/root/404.html");
                    response.setStatusCode(400);
                    response.setStatusReason("NotFound");
                    response.setsFile(fourFile);
                }
            }

            response.flush();
            System.out.println("响应头发送完毕!");


        }catch (EmptyRequestException e){
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
