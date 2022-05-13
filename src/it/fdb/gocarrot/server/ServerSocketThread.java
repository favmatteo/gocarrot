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

    private int clientNo;

    /**
     * Costruttore classe ServerSocketThread, si occupa di istanziare i vari oggetti
     * @param inSocket socket collegamento client
     * @param counter numero del client collegato al server
     * @param multithreadedSocketServer istanza MultithreadedSocketServer
     */
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

    /**
     * Metodo run
     * Il socket resta in ascolto finchè non riceve un messaggio quit
     * se il socket riceve un messaggio che contiene la parola MyNumbers allora invia
     * al client il suo numero col quale è assegnato al server, altrimenti invia al client
     * i dati (num client, posizione x, posizione y, livello) dell'altro player
     */
    public void run(){
        try {
            String clientMessage = "";
            while (!clientMessage.equals("quit")){
                if(multithreadedSocketServer.getServerSocketThreads().size() == 1){
                    System.out.println(clientNo + " diventa 1");
                    clientNo = 1;
                }

                clientMessage = inputStream.readUTF();

                String[] clientMessages = clientMessage.split(" ");
                if(clientMessage.contains("myNumber")){
                    outputStream.writeUTF(String.valueOf(clientNo));
                }
                else{
                    for(ServerSocketThread serverSocketThread : multithreadedSocketServer.getServerSocketThreads()){
                        if(serverSocketThread.clientNo != clientNo)
                            serverSocketThread.outputStream.writeUTF(clientMessages[1] + " " + clientMessages[2] + " " + clientMessages[3] + " " + clientMessages[4]); // [num client] x y level timing
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
