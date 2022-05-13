package it.fdb.gocarrot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

/**
 * Metodo che rappresenta il client
 * permette la connessione a un socket, il ricevimento e l'invio di dati
 */
public class Client {
    private final Socket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private final Board board;

    public Client(Board board){
        try {
            socket = new Socket("localhost", 5050);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            this.board = board;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invia coordinate x e y insieme al numero client al server
     * @param clientNo numero client
     * @throws IOException Lancia eccezzione in caso di problemi con la comunicazione col server
     */
    public void sendCoordinates(int clientNo) throws IOException {
        dataOutputStream.writeUTF(clientNo + " " + (board.getPlayer().getX() - board.getCamera().getX()) + " " + board.getPlayer().getY() + " " + board.getPlayer().getLevel() + " " +
            board.getPlayer().getTimer().getSecondi());
    }

    /**
     * Riceve un messaggio dal server
     * @return messaggio
     * @throws IOException Lancia eccezzione in caso di problemi con la comunicazione col server
     */
    public String getMessage() throws IOException {
        return dataInputStream.readUTF();
    }

    /**
     * Chiude la sessione con il socket, provocando la distruzione dell'istanza ServerSocketThread e quindi la chiusura del socket stesso
     * @throws IOException Lancia eccezzione in caso di problemi con la comunicazione col server
     */
    public void close() throws IOException {
        dataOutputStream.writeUTF("quit");
    }

    /**
     * Fa una richiesta al socket per conoscere il numero ID
     * @return numero ID
     * @throws IOException Lancia eccezzione in caso di problemi con la comunicazione col server
     */
    public int getNumberID() throws IOException {
        dataOutputStream.writeUTF("myNumber");
        String risposta = dataInputStream.readUTF();
        return Integer.parseInt(risposta);
    }

    /**
     * Chiede al server il numero di client connessi
     */
    public int askNumberClient() throws IOException {
        dataOutputStream.writeUTF("nClient");
        String risposta = dataInputStream.readUTF();
        return Integer.parseInt(risposta);
    }

    public void sendTime(int clientNo) throws IOException {
        dataOutputStream.writeUTF(clientNo + " " + board.getPlayer().getTimer().getSecondi());
    }
}
