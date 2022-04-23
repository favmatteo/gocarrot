package it.fdb.gocarrot.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public final class ServerSocketThread extends Thread {
    Socket serverClient;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    MultithreadedSocketServer multithreadedSocketServer;

    int clientNo;

    public ServerSocketThread(Socket inSocket, int counter, MultithreadedSocketServer multithreadedSocketServer){
        this.serverClient = inSocket;
        this.clientNo = counter;
        this.multithreadedSocketServer = multithreadedSocketServer;
    }

    public void run(){
        try {
            inputStream = new DataInputStream(serverClient.getInputStream());
            outputStream = new DataOutputStream(serverClient.getOutputStream());
            String serverMessage="", clientMessage="";
            while (!clientMessage.equals("quit")){
                clientMessage = inputStream.readUTF();

                String[] clientMessages = clientMessage.split(" ");
                if(clientMessage.contains("myNumber")){
                    serverMessage = String.valueOf(clientNo);
                    outputStream.writeUTF(serverMessage);
                }
                else{
                    for(ServerSocketThread serverSocketThread : multithreadedSocketServer.serverSocketThreads){
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
            multithreadedSocketServer.serverSocketThreads.remove(this);
            System.out.println("Bye " + clientNo + "!\n");
        }
    }
}
