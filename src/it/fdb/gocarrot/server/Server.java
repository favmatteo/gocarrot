package it.fdb.gocarrot.server;

public class Server {
    public static void main(String[] args) {
        MultithreadedSocketServer multithreadedSocketServer = new MultithreadedSocketServer();
        multithreadedSocketServer.run();
    }
}
