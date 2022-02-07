package com.iti.tictactoeserver.helpers.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener  {
    ServerSocket server;
    DataInputStream dis;
    PrintStream ps;

    public ClientListener(){
        try{
            server=new ServerSocket(5001);
            System.out.println(server.getLocalPort());
            while(true){
                Socket socket= server.accept();
                new ClientHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientListener server=new ClientListener();
    }


}
