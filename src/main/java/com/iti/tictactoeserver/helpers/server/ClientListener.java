package com.iti.tictactoeserver.helpers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener extends Thread {

    @Override
    public void run() {
        ClientHandler.initPlayerList();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            while (true) {
                System.out.println("Waiting for a new client to connect... ");
                Socket socket = serverSocket.accept();
                System.out.println("Connected Successfully");
                new ClientHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
