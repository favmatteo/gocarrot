package it.fdb.gocarrot;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Classe che rappresenta l'avversario (colui che si collega online)
 */
public class Opponent extends Thread {
    private final Board board;
    private final Player player;
    private final Client client;
    private int x;
    private int y;

    private boolean visible = false;

    private String messaggio;

    private int tempoPartita;


    public Opponent(Board board, Player player, int x, int y) {
        this.board = board;
        this.player = player;
        client = player.getClient();
        this.x = x;
        this.y = y;
        this.tempoPartita = 0;
    }

    /**
     * Funzione che si occupa di disegnare l'avversario
     * @param graphics2D graphics2D
     */
    public void draw(Graphics2D graphics2D) {
        if(visible) {
            graphics2D.setColor(Color.PINK);
            graphics2D.setColor(new Color(204, 119, 34, 90));
            graphics2D.fillRoundRect(x, y, Player.WIDTH, Player.HEIGHT, 70, 70);
            graphics2D.setColor(new Color(106, 170, 18, 90));
            graphics2D.setStroke(new BasicStroke(10));
            graphics2D.draw(new Line2D.Float(x + 20, y + 5, x - 5, y - 15));
            graphics2D.draw(new Line2D.Float(x + 30, y + 5, x + 50 + 5, y - 15));
            graphics2D.draw(new Line2D.Float(x + 25, y + 7, x + 25, y - 25));
            graphics2D.setColor(new Color(174, 102, 30, 90));
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawArc(x + 3, y + 20, 40, 10, 0, -180);
            graphics2D.drawArc(x + 7, y + 40, 40, 10, 0, -130);
            graphics2D.drawArc(x + 4, y + 60, 40, 10, 180, 110);
        }
    }

    /**
     * Metodo che viene avviato dal thread, invia al server le coordinate del player,
     * riceve le coordinate dell'altro player e in base al messaggio che riceve aggiorna le sue coordinate
     */
    @Override
    public void run(){
        while (true) {
            try {
                messaggio = client.getMessage();
                String[] coordinates = messaggio.split(" ");
                x = Integer.parseInt(coordinates[0]) + this.board.getCamera().getX();
                y = Integer.parseInt(coordinates[1]);
                visible = Integer.parseInt(coordinates[2]) == player.getLevel(); // rende visibile l'opponent
                                                                                // solo se si trovano sullo stesso livello
                tempoPartita = Integer.parseInt(coordinates[3]); // tempo
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getTempoPartita() {
        return tempoPartita;
    }
}
