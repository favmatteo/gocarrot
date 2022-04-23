package it.fdb.gocarrot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultithreadedSocketServer {
    private final ArrayList<ServerSocketThread> serverSocketThreads;
    private final ServerSocket server;

    public MultithreadedSocketServer(){
        serverSocketThreads = new ArrayList<>();
        try {
            server = new ServerSocket(5050);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        try {
            System.out.println("Server started....");
            while (true) {
                if(serverSocketThreads.size() < 2) {
                    Socket serverClient = server.accept(); // il server accetta la richiesta del client
                    System.out.println("Client n° " + (serverSocketThreads.size() + 1) + " started!");
                    ServerSocketThread sct = new ServerSocketThread(serverClient, serverSocketThreads.size() + 1, this);
                    sct.start();
                    serverSocketThreads.add(sct);
                }
            }
        } catch (Exception e) {
            System.err.println("Error");
        }
    }

    public ArrayList<ServerSocketThread> getServerSocketThreads() {
        return serverSocketThreads;
    }
}

// http://net-informations.com/java/net/multithreaded.htm