package it.fdb.gocarrot.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class ServerSocketThread extends Thread {
    private final Socket serverClient;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private final MultithreadedSocketServer multithreadedSocketServer;

    private final int clientNo;

    public ServerSocketThread(Socket inSocket, int counter, MultithreadedSocketServer multithreadedSocketServer){
        this.serverClient = inSocket;
        this.clientNo = counter;
        this.multithreadedSocketServer = multithreadedSocketServer;
        try {
            inputStream = new DataInputStream(serverClient.getInputStream());
            outputStream = new DataOutputStream(serverClient.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        try {
            String clientMessage = "";
            while (!clientMessage.equals("quit")){
                clientMessage = inputStream.readUTF();

                String[] clientMessages = clientMessage.split(" ");
                if(clientMessage.contains("myNumber")){
                    outputStream.writeUTF(String.valueOf(clientNo));
                }
                else{
                    for(ServerSocketThread serverSocketThread : multithreadedSocketServer.getServerSocketThreads()){
                        if(serverSocketThread.clientNo != clientNo)
                            serverSocketThread.outputStream.writeUTF(clientMessages[1] + " " + clientMessages[2] + " " + clientMessages[3]); // [num client] x y level
                    }
                }
            }
            inputStream.close();
            outputStream.close();
            serverClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            multithreadedSocketServer.getServerSocketThreads().remove(this);
            System.out.println("Bye " + clientNo + "!\n");
        }
    }
}
