package com.iti.tictactoeserver.helpers.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;

public class ClientHandler extends Thread {

    static Vector<ClientHandler> clients = new Vector<>();
    private PrintStream printStream;
    private DataInputStream dataInputStream;

    public ClientHandler(Socket socket) {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            clients.add(this);
            System.out.println("No. Clients: " + clients.size());
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
