package com.iti.tictactoeserver.helpers.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;

public class ClientHandler extends Thread  {
    DataInputStream dis;
    PrintStream ps;
    static Vector<ClientHandler> clientsvector = new Vector<ClientHandler>();

    public ClientHandler(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        ps = new PrintStream(socket.getOutputStream());
        clientsvector.add(this);
        start();

    }

    @Override
    public void run() {
        while (true) {
            try{
                String msg= dis.readLine();
                System.out.println(msg);
                if (msg == null) {
                    break;
                }
                sendToAll(msg);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            dis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ps.close();
        clientsvector.remove(this);
    }

    public void sendToAll(String msg) {
        for (ClientHandler ch : clientsvector) {
            ch.ps.println(msg);
            ch.ps.println("hello from the other side");
            System.out.println(msg);
        }
    }

}
