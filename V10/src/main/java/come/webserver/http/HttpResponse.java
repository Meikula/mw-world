package come.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private Socket socket;
    private File sFile;
    private String statusReason = "OK";
    private int statusCode=200;
    private Map<String,String> headers = new HashMap<>();
    private byte[] contentData;

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }
    public void flush(){
        sendStatusLine();
        sendHeaders();
        sendContent();



    }
    private void sendStatusLine(){
        try {
            OutputStream out = socket.getOutputStream();
            String line ="HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("UTF-8"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendHeaders(){
        try {
            OutputStream out = socket.getOutputStream();
            Set<Map.Entry<String,String>> set = headers.entrySet();
            for(Map.Entry<String,String> e:set){
                String key = e.getKey();
                String value = e.getValue();
                headers.put(key,value);
                String line =key+": "+value;
                out.write(line.getBytes("UTF-8"));
                out.write(13);
                out.write(10);
                System.out.println("响应头:"+line);
            }
            out.write(13);
            out.write(10);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendContent(){
        try {
            if(contentData!=null){
                OutputStream out = socket.getOutputStream();
                out.write(contentData);
            }else if (sFile!=null){
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(sFile);
                int d;
                byte[]data = new byte[1024*10];
                while((d=fis.read(data))!=-1){
                    out.write(data,0,d);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("响应正文发送完毕");
    }

    public void setsFile(File sFile) {
        this.sFile = sFile;
        int index = sFile.getName().lastIndexOf(".")+1;
        String line= sFile.getName().substring(index);
        String value= HttpContext.getMimeMapping(line);
        putHeader("Content-Type",value);
        putHeader("Content-Length",sFile.length()+"");
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String putHeader(String key,String value){
        return headers.put(key,value);
    }
    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
        putHeader("Content-Length",contentData.length+"");
    }
}
