package it.fdb.gocarrot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final Board board;

    public Client(Board board){
        try {
            socket = new Socket("192.168.1.246", 5050);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            this.board = board;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCoordinates(int clientNo) throws IOException {
        dataOutputStream.writeUTF(clientNo + " " + (board.getPlayer().getX() - board.getCamera().getX()) + " " + board.getPlayer().getY() + " " + board.getPlayer().getLevel());
    }

    public void close() throws IOException {
        dataOutputStream.writeUTF("quit");
    }

    public String getMessage() throws IOException {
        String utf = dataInputStream.readUTF();
        return utf;
    }

    public int getNumberID() throws IOException {
        dataOutputStream.writeUTF("myNumber");
        System.out.println();
        String risposta = dataInputStream.readUTF();
        return Integer.parseInt(risposta);
    }
}
