package come.webserver.servlet;

import come.webserver.http.HttpRequest;
import come.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RegServlet {

    public void service(HttpRequest request, HttpResponse response){
        String username=request.getParameters("username");
        String password=request.getParameters("password");
        String nickname=request.getParameters("nickname");
        String strAge=request.getParameters("age");
        System.out.println(username+","+password+","+nickname+","+strAge);
        if(username==null||password==null||nickname==null||strAge==null||!strAge.matches("[0-9]+")){
            File file = new File("./webapps/myweb/reg_input_error.html");
            response.setsFile(file);
            return;
        }

        int age = Integer.parseInt(strAge);
    try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");)
    {
        for(int i=0;i<raf.length()/100;i++){
            raf.seek(i*100);
            byte data[] = new byte[32];
            raf.read(data);
            String name = new String(data,"UTF-8").trim();
            if(name.equals(username)){
                File file = new File("./webapps/myweb/have_user.html");
                response.setsFile(file);
                return;

            }
        }
        raf.seek(raf.length());
        byte[]data = username.getBytes("UTF-8");
        data = Arrays.copyOf(data,32);
        raf.write(data);

        data = password.getBytes("UTF-8");
        data = Arrays.copyOf(data,32);
        raf.write(data);
        data = nickname.getBytes("UTF-8");
        data = Arrays.copyOf(data,32);
        raf.write(data);

        raf.writeInt(age);

        File file = new File("./webapps/myweb/reg_success.html");
        response.setsFile(file);

    }catch(IOException e){
        e.printStackTrace();

    }
    }
}
