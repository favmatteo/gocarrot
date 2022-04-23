package it.fdb.gocarrot.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultithreadedSocketServer {
    ArrayList<ServerSocketThread> serverSocketThreads = new ArrayList<>();
    public void run() {
        try {
            ServerSocket server = new ServerSocket(5050);
            System.out.println("Server started....");
            while (true) {
                if(serverSocketThreads.size() < 2) {
                    Socket serverClient = server.accept(); // il it.fdb.gocarrot.server accetta la richiesta del client
                    System.out.println("Client N. " + (serverSocketThreads.size() + 1) + " started!");
                    ServerSocketThread sct = new ServerSocketThread(serverClient, serverSocketThreads.size() + 1, this);
                    sct.start();
                    serverSocketThreads.add(sct);
                }
            }
        } catch (Exception e) {
            System.err.println("Error");
        }
    }
}

// http://net-informations.com/java/net/multithreaded.htm