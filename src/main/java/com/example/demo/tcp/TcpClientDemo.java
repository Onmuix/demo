package com.example.demo.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClientDemo {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream outputStream = null;
        try {
            //1、要知道服务器的地址，端口编号
            InetAddress serverIP = InetAddress.getByName("127.0.0.1");
            int port = 9999;
            //2、创建一个socket连接
            socket = new Socket(serverIP, port);
            //3、发送消息 IO流
            outputStream = socket.getOutputStream();
            outputStream.write("你好".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
