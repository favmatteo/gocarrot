package it.fdb.gocarrot;

import it.fdb.gocarrot.blocks.Block;
import it.fdb.gocarrot.blocks.gravityblocks.Sand;
import it.fdb.gocarrot.blocks.simple.Grass;
import it.fdb.gocarrot.blocks.special.*;
import it.fdb.gocarrot.bonus.Coin;
import it.fdb.gocarrot.bonus.GenericBonus;
import it.fdb.gocarrot.bonus.Shield;
import it.fdb.gocarrot.element.Checkpoint;
import it.fdb.gocarrot.element.Finish;
import it.fdb.gocarrot.element.GenericElement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    }

    public void setSpeed(){
        try {
            client.sendCoordinates(clientNo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(y < 500) {
            if (keyLEFT && board.getCamera().getX() < 0){
                xSpeed--;
            }
            else if (keyRIGHT){
                xSpeed++;
            }
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

        boolean kdown = true;
        if(keyDOWN){
            for(ArrayList<Object> strato : board.getMappa()) {
                for (Object element : strato) {
                    // il personaggio può saltare quando tocca il suolo
                    if(element instanceof Block && ySpeed > 1) {
                        if (!((Block) element).getHitBox().intersects(hitBox) && kdown) {
                            ySpeed += 0.5;
                            kdown = false;
                        }
                    }
                }
            }
        }



        // orizzontale collisione
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

        // vari controlli
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
                        if(element instanceof Checkpoint) { /*TODO: implementazione*/ }
                        if(element instanceof Finish){ this.completedLevel= true; }
                    }
                }
            }
        }


        board.getCamera().setX((int)(board.getCamera().getX() - xSpeed));
        y += ySpeed;

        hitBox.x = x;
        hitBox.y = y;

        // Controlla se il player è morto
        if(y > 550){
            board.reset();
        }
    }

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
        if(numShield >= 1){
            graphics2D.setColor(Color.decode("#82766f"));
            graphics2D.fillRect(x + 20, y + 40, 32, 36);
            graphics2D.fillPolygon(new int[]{x + 21, x + 52, x + 36}, new int[]{y + 76, y + 76, y + 81}, 3);
            graphics2D.setColor(Color.decode("#ad7658"));
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.drawLine(x + 21, y + 66, x + 51, y + 66);
            graphics2D.drawLine(x + 36, y + 41, x + 36, y + 79);
        }

        /**
        // cavallo
        graphics2D.setColor(Color.decode("#662200"));
        graphics2D.fillRoundRect(200, 195, 150, 55, 70, 70);
        // zampe
        graphics2D.setStroke(new BasicStroke(8));
        graphics2D.draw(new Line2D.Float(225, 250, 225, 300));
        graphics2D.draw(new Line2D.Float(250, 250, 250, 300));
        graphics2D.draw(new Line2D.Float(305, 250, 305, 300));
        graphics2D.draw(new Line2D.Float(330, 250, 330, 300));
        // cosce
        graphics2D.fillOval(280,195,70,65);
        graphics2D.fillPolygon(new Polygon(new int[]{250,305,310},new int[]{250,250,260}, 3));
        graphics2D.fillOval(205,205,70,50);
        graphics2D.fillPolygon(new Polygon(new int[]{300,355,333},new int[]{230,230,275}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{275,330,308},new int[]{230,230,275}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{201,255,223},new int[]{230,230,270}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{235,280,248},new int[]{230,230,270}, 3));
        // coda
        graphics2D.setColor(Color.decode("#4d1a00"));
        graphics2D.fillPolygon(new Polygon(new int[]{343,355,370},new int[]{205,265,255}, 3));
        // collo
        graphics2D.setColor(Color.decode("#662200"));
        graphics2D.fillRoundRect(200, 160, 45, 90, 70, 70);
        // testa con mandibola
        graphics2D.fillRoundRect(165, 150, 70, 30, 70, 70);
        graphics2D.fillRoundRect(165, 180, 70, 15, 70, 70);
        // orecchie
        graphics2D.fillPolygon(new Polygon(new int[]{240,210,220},new int[]{190,190,130}, 3));
        // occhio
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(205,150,15,15);
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(205,150,9,9);
        // dentini
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(180,178,3,5);
        graphics2D.fillRect(175,176,3,5);
        graphics2D.fillRect(185,178,3,5);
        graphics2D.fillRect(190,178,3,5);
        // criniera
        graphics2D.setColor(Color.decode("#4d1a00"));
        graphics2D.fillPolygon(new Polygon(new int[]{228,240,260},new int[]{152,210,210}, 3));
         */
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
}

// this.board.getMappa().get(livello).set(elemento, new Stone(((Spike) element).getX() - board.getCamera().getX(), ((Spike) element).getY()));
