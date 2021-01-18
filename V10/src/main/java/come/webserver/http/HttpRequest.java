package come.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private Socket socket;
    private String method;
    private String uri;
    private String protocol;
    private Map<String,String> headsers = new HashMap<>();
    private String requestURI;
    private String queryString;
    private Map<String,String> parameters = new HashMap<>();


    public HttpRequest(Socket socket) throws EmptyRequestException{
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }
    private void parseRequestLine()throws EmptyRequestException{
        try {
            String line = readLine();
            String []data = line.split(" ");
            method=data[0];
            uri=data[1];
            protocol=data[2];
            parseUri();
            System.out.println("method:"+data[0]);
            System.out.println("uri:"+data[1]);
            System.out.println("protocol:"+data[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseUri(){
        try {
            uri= URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String data [] = uri.split("\\?");
            requestURI=data[0];
            if(data.length>1){
                queryString=data[1];
                data=queryString.split("&");
                for(String s : data){
                    String arry[]=s.split("=");
                    if(arry.length>1){
                        parameters.put(arry[0],arry[1]);
                    }else{
                        parameters.put(arry[0],null);
                    }
                }
            }
            System.out.println("HttpRequest:进一步解析uri完毕!");
        }else{
            requestURI=uri;
        }
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);

    }
    private void parseHeaders(){
        while (true){
            try {
                String line = readLine();
                if(line.isEmpty()){
                    break;
                }
                System.out.println("请求行:"+line);
                String[]data = line.split(": ");
                headsers.put(data[0],data[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("headers:"+headsers);

    }
    private void parseContent(){
        System.out.println("HttpRequest:解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");
    }
    private String readLine()throws IOException {
        InputStream is = socket.getInputStream();
        int d;
        char old='a';
        char now='a';
        StringBuilder builder = new StringBuilder();
        while ((d=is.read())!=-1){
            now=(char)d;
            if(old==13&&now==10){
                break;
            }
            builder.append(now);
            old = now;
        }
        return builder.toString().trim();
    }

    public String getUri() {
        return uri;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
