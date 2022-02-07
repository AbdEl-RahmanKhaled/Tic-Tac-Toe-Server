package com.iti.tictactoeserver.helpers.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {

    static List<ClientHandler> clients = new ArrayList<>();
    private PrintStream printStream;
    private DataInputStream dataInputStream;
    private Socket clientSocket;
    private int id;
    public ClientHandler(Socket socket) {
        id = 10;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            clientSocket = socket;
            clients.add(this);
            List<ClientHandler> c =  clients.stream().filter(clientHandler -> clientHandler.id != 10).collect(Collectors.toList());
            System.out.println(c.size());
            System.out.println(c.get(0).id);
            System.out.println("No. Clients: " + clients.size());
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
