package come.webserver.servlet;

import come.webserver.http.HttpRequest;
import come.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw"))
        {
            for(int i=0;i<raf.length()/100;i++){
                byte[]data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                String username=request.getParameters("username");
                System.out.println(username);
                System.out.println(name);
                if(name.equals(username)){
                    System.out.println("1111111112");
                    raf.read(data);
                    String pw = new String(data,"UTF-8").trim();
                    String password = request.getParameters("password");
                    if(pw.equals(password)){
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setsFile(file);
                        return;
                    }
                    break;
                }

            }
            File file = new File("./webapps/myweb/login_fail.html");
            response.setsFile(file);


        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
