package it.fdb.gocarrot;

import it.fdb.gocarrot.blocks.Block;
import it.fdb.gocarrot.blocks.simple.Grass;
import it.fdb.gocarrot.blocks.special.*;
import it.fdb.gocarrot.bonus.Coin;
import it.fdb.gocarrot.bonus.GenericBonus;
import it.fdb.gocarrot.bonus.Shield;
import it.fdb.gocarrot.element.Finish;
import it.fdb.gocarrot.element.GenericElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.desktop.SystemSleepEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final Board board;
    private int x;
    private int y;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 100;

    private double xSpeed; // > 0 destra, < 0 sinistra
    private double ySpeed; // > 0 giù, < 0 su

    private final Rectangle hitBox;

    private boolean keyUP;
    private boolean keyDOWN;
    private boolean keyRIGHT;
    private boolean keyLEFT;

    private boolean playerOnSlime;
    private boolean playerOnIce;

    private int score;

    private int numShield;

    private boolean completedLevel;

    private final Client client;
    private final int clientNo;

    private int level = 1;

    private final Timer timer;

    private boolean visible;

    /**
     * Costruttore che inizializza un oggetto di tipo Player
     * Posiziona il giocatore sull'asse x e y, crea una sua hitbox
     * rappresentata da un rettangolo di dimensioni pari a quelle del player
     * inizializza poi un oggetto di tipo Client che permette di comunicare
     * le coordinate del player al server tramite socket, una volta inizializzato
     * l'oggetto fa una richiesta al server per sapere il suo number ID
     * (durante le comunicazioni al server il messaggio spedito contiene un number ID
     * che indica il numero della sessione, essendoci solamente due player il number ID
     * assume il valore 1 o 2)
     * @param board board iniziale
     * @param x posizione sull'asse delle x iniziale
     * @param y posizione sull'asse delle y iniziale
     */
    public Player(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x, y, WIDTH, HEIGHT);
        this.client = new Client(board);
        try {
            this.clientNo = client.getNumberID();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this.clientNo);
        this.timer = new Timer();
        this.visible = true;
    }


    /**
     * Metodo che si occupa di gestire il player, i movimenti, le collisioni
     */
    public void move(){
        // Invia le coordinate del player al server
        try {
            client.sendCoordinates(clientNo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // se il personaggio è ancora nello schermo
        if(y < 500) {
            // se preme freccia sinistra e non va fuori dal campo della telecamera
            if (keyLEFT && board.getCamera().getX() < 0){
                xSpeed--;
            }
            // se preme freccia destra
            else if (keyRIGHT){
                xSpeed++;
            }
            // altrimenti se nessuno dei due è premuto vai avanti per un po'
            else{
                xSpeed *= 0.8;
            }

            // toglie lo sliding (il personaggio continua ad andare avanti)
            if(!playerOnIce) {
                if (xSpeed > 0 && xSpeed < 1) xSpeed = 0;
                if (xSpeed < 0 && xSpeed > -1) xSpeed = 0;
            }

            // limità velocità massima
            if(playerOnSlime){
                if (xSpeed > 5) xSpeed = 2.5;
                if (xSpeed < -5) xSpeed = -2.5;
            }else if(playerOnIce){
                if (xSpeed > 5) xSpeed = 7;
                if (xSpeed < -5) xSpeed = -7;
            }else{
                if (xSpeed > 5) xSpeed = 5;
                if (xSpeed < -5) xSpeed = -5;
            }

            // salto
            if (keyUP) {
                hitBox.y += 1;
                for(ArrayList<Object> strato : board.getMappa()) {
                    for (Object element : strato) {
                        if(element instanceof Block) {
                            // il personaggio può saltare quando tocca il suolo
                            if (((Block) element).getHitBox().intersects(hitBox) && !(element instanceof Slime)) {
                                ySpeed = -10;
                            }
                        }
                    }
                }
            }
        }
        ySpeed += 0.3;

        // kdown = discesa rapida dopo salto
        boolean kdown = true;
        if(keyDOWN){
            for(ArrayList<Object> strato : board.getMappa()) {
                for (Object element : strato) {
                    if(element instanceof Block && ySpeed > 1) {
                        if (!((Block) element).getHitBox().intersects(hitBox) && kdown) {
                            ySpeed += 0.5;
                            kdown = false;
                        }
                    }
                }
            }
        }



        // collisione orizzontale
        hitBox.x += xSpeed;
        for(ArrayList<Object> strato : board.getMappa()) {
            for (Object element : strato) {
                if(element instanceof Block) {
                    if (hitBox.intersects(((Block) element).getHitBox())) {
                        hitBox.x -= xSpeed;
                        while (!((Block) element).getHitBox().intersects(hitBox)) {
                            hitBox.x += Math.signum(xSpeed); // ritorna 0 se xSpeed è 0, -1 se xSpeed è < 0, +1 se xSpeed > 0
                        }
                        hitBox.x -= Math.signum(xSpeed);
                        board.getCamera().setX(board.getCamera().getX() + x - hitBox.x);
                        xSpeed = 0;
                        hitBox.x = x;
                    }
                }
            }
        }

        // collisione verticale con blocco
        hitBox.y += ySpeed;
        for(ArrayList<Object> strato : board.getMappa()) {
            for (Object element : strato) {
                if(element instanceof Block) {
                    if (hitBox.intersects(((Block) element).getHitBox())) {
                        kdown = true;
                        hitBox.y -= ySpeed;
                        while (!((Block) element).getHitBox().intersects(hitBox)) {
                            hitBox.y += Math.signum(ySpeed);
                        }
                        hitBox.y -= Math.signum(ySpeed);
                        ySpeed = 0;
                        y = hitBox.y;
                    }
                }
            }
        }

        // collisione con blocchi speciali
        // come slime, ghiaccio, spine, trampolino, salto kdown terra, coins, scudo, CheckPoint e fine
        hitBox.y = (int) (hitBox.y + ySpeed + 1);
        for(ArrayList<Object> strato : board.getMappa()) {
            for (Object element : strato) {
                if(element instanceof Block) {
                    if (hitBox.intersects(((Block) element).getHitBox())) {
                        this.playerOnSlime = element instanceof Slime;
                        this.playerOnIce = element instanceof Ice;
                        if(element instanceof Spike && ((Spike) element).isActive() && Math.abs(x - ((Spike) element).getX()) < (WIDTH / 2)) {
                            if(numShield >= 1){ ((Spike) element).setActive(false); numShield--; break; }
                            else{ this.board.reset(); }
                            hitBox.y = (int) (hitBox.y - (ySpeed - 1));
                        }
                        if (element instanceof Trampoline) { ySpeed = -15; }
                        if (element instanceof Grass && !kdown) { ((Grass) element).setBrokenBlock(true); }
                    }
                }else if(element instanceof GenericBonus){
                    if (hitBox.intersects(((GenericBonus) element).getHitBox()) && ((GenericBonus) element).isVisible()) {
                        if(element instanceof Coin) {
                            ((Coin) element).setVisible(false);
                            score++;
                        }
                        if(element instanceof Shield) {
                            ((Shield) element).setVisible(false);
                            numShield++;
                        }
                    }
                }else if(element instanceof GenericElement){
                    if(hitBox.intersects(((GenericElement) element).getHitBox())){
                        if(element instanceof Finish){ this.completedLevel= true; }
                    }
                }
            }
        }


        // muovi il personaggio insieme alla telecamera
        board.getCamera().setX((int)(board.getCamera().getX() - xSpeed));
        y += ySpeed;

        hitBox.x = x;
        hitBox.y = y;

        // Controlla se il player è morto
        if(y > 550){
            board.reset();
        }
    }

    /**
     * Funzione che si occupa di disegnare il player
     * @param graphics2D graphics2D
     */
    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(Color.decode("#CC7722"));
        graphics2D.fillRoundRect(x, y, WIDTH, HEIGHT, 70, 70);
        graphics2D.setColor(Color.decode("#6aaa12"));
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.draw(new Line2D.Float(x + 20, y + 5, x - 5, y - 15));
        graphics2D.draw(new Line2D.Float(x + 30, y + 5, x + 50 + 5, y - 15));
        graphics2D.draw(new Line2D.Float(x + 25, y + 7, x + 25, y - 25));
        graphics2D.setColor(Color.decode("#AE661E"));
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawArc(x + 3, y + 20, 40, 10, 0, -180);
        graphics2D.drawArc(x + 7, y + 40, 40, 10, 0, -130);
        graphics2D.drawArc(x + 4, y + 60, 40, 10, 180, 110);
        if (numShield >= 1) {
            graphics2D.setColor(Color.decode("#82766f"));
            graphics2D.fillRect(x + 20, y + 40, 32, 36);
            graphics2D.fillPolygon(new int[]{x + 21, x + 52, x + 36}, new int[]{y + 76, y + 76, y + 81}, 3);
            graphics2D.setColor(Color.decode("#ad7658"));
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.drawLine(x + 21, y + 66, x + 51, y + 66);
            graphics2D.drawLine(x + 36, y + 41, x + 36, y + 79);
        }
    }

    public void setKeyUP(boolean keyUP) {
        this.keyUP = keyUP;
    }

    public void setKeyDOWN(boolean keyDOWN) {
        this.keyDOWN = keyDOWN;
    }

    public void setKeyRIGHT(boolean keyRIGHT) {
        this.keyRIGHT = keyRIGHT;
    }

    public void setKeyLEFT(boolean keyLEFT) {
        this.keyLEFT = keyLEFT;
    }

    public void reset(){
        this.x = 100;
        this.y = 500;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.score = 0;
        this.numShield = 0;
    }

    // TODO: rimuovere se non necessario
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }


    public int getNumShield() {
        return numShield;
    }

    public boolean hasShield() {
        return numShield >= 1;
    }

    public void setCompletedLevel(boolean completedLevel) {
        this.completedLevel = completedLevel;
    }

    public boolean isCompletedLevel() {
        return completedLevel;
    }

    public int getClientNo() {
        return clientNo;
    }

    public Client getClient() {
        return client;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public Timer getTimer() {
        return timer;
    }
}

// this.board.getMappa().get(livello).set(elemento, new Stone(((Spike) element).getX() - board.getCamera().getX(), ((Spike) element).getY()));
