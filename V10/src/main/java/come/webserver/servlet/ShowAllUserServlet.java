package come.webserver.servlet;

import come.webserver.http.HttpRequest;
import come.webserver.http.HttpResponse;
import come.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ShowAllUserServlet {
    List<User> list = new ArrayList<>();
    public void service(HttpRequest request, HttpResponse response){

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw"))
        {
            for(int i=0;i<raf.length()/100;i++) {
                byte[] data = new byte[32];
                raf.read(data);
                String username = new String(data, "UTF-8").trim();

                raf.read(data);
                String password = new String(data, "UTF-8").trim();

                raf.read(data);
                String nickname = new String(data, "UTF-8").trim();

                int age = raf.readInt();

                list.add(new User(username, password, nickname, age));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        for(User user:list){
            System.out.println(user);
        }
        Context context = new Context();
        context.setVariable("users",list);

        FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
        fileTemplateResolver.setTemplateMode("html");
        fileTemplateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine t = new TemplateEngine();
        t.setTemplateResolver(fileTemplateResolver);

        String html = t.process("./webapps/myweb/userlist.html",context);
        System.out.println(html);

        try {
            byte data[] = html.getBytes("UTF-8");
            response.setContentData(data);
            response.putHeader("Content-Type","text/html");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}
