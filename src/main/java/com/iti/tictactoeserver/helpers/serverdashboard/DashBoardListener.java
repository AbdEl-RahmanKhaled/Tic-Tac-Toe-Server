package com.iti.tictactoeserver.helpers.serverdashboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DashBoardListener extends Thread {
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5001);
            while (true) {
                System.out.println("Waiting for a new dashboard to connect... ");
                Socket socket = serverSocket.accept();
                System.out.println("Connected Successfully");
                new DashBoardHandler(socket);
            }
        } catch (IOException e) {
            System.out.println("Server Stopped");
        }
    }


    @Override
    public void interrupt() {
        super.interrupt();
        try {
            serverSocket.close();
            DashBoardHandler.stopAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
