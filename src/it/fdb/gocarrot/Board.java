package it.fdb.gocarrot;

import it.fdb.gocarrot.blocks.Block;
import it.fdb.gocarrot.blocks.gravityblocks.Sand;
import it.fdb.gocarrot.blocks.simple.Grass;
import it.fdb.gocarrot.blocks.special.Spike;
import it.fdb.gocarrot.bonus.GenericBonus;
import it.fdb.gocarrot.element.GenericElement;
import it.fdb.gocarrot.element.Horse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel {

    private final Player player;
    private final Timer timer;
    private final Opponent opponent;
    private ArrayList<ArrayList<Object>> mappa;
    private final Camera camera;
    private final MapReader mapReader;
    private final Audio audio;

    /**
     * Costruttore, inizializza oggetti, crea un timer scheduler
     * il quale ogni tot tempo esegue delle azioni
     */
    public Board() {
        player = new Player(this, 100, 480);
        opponent = new Opponent(this, player, 100, 480);
        timer = new Timer();
        mapReader = new MapReader(this);
        audio = new Audio("GameSound.wav");
        loadMap(player.getLevel());
        audio.play();

        camera = new Camera();
        setBackground(Color.decode("#87CEFF"));

        opponent.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Toolkit.getDefaultToolkit().sync(); // su linux
                player.set();
                for(ArrayList<Object> strato : mappa){
                    for(Object element : strato) {
                        if(element instanceof Block) ((Block)element).set(camera);
                        if(element instanceof GenericBonus) ((GenericBonus)element).set(camera);
                        if(element instanceof GenericElement) ((GenericElement)element).set(camera);
                        if(element instanceof Sand) ((Sand) element).gravity();
                    }
                }
                if(player.isCompletedLevel()){
                    mappa.clear();
                    player.setLevel(player.getLevel() + 1);
                    loadMap(player.getLevel());
                    player.setCompletedLevel(false);
                    reset();
                }

                repaint();

            }
        }, 0, 15);
    }

    /**
     * Controlla se un tasto è premuto
     * @param e evento tasto premuto
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // virtual key
        if(key == KeyEvent.VK_LEFT) player.setKeyLEFT(true);
        if(key == KeyEvent.VK_RIGHT) player.setKeyRIGHT(true);
        if(key == KeyEvent.VK_UP) player.setKeyUP(true);
        if(key == KeyEvent.VK_DOWN) player.setKeyDOWN(true);
    }

    /**
     * Controlla se un tasto è rilasciato
     * @param e evento tasto rilasciato
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode(); // virtual key
        if(key == KeyEvent.VK_LEFT) player.setKeyLEFT(false);
        if(key == KeyEvent.VK_RIGHT) player.setKeyRIGHT(false);
        if(key == KeyEvent.VK_UP) player.setKeyUP(false);
        if(key == KeyEvent.VK_DOWN) player.setKeyDOWN(false);
    }

    /**
     * Resetta impostazione player ad esempio alla morte.
     */
    public void reset(){
        player.reset();
        for(ArrayList<Object> strato : mappa){
            for(Object element : strato) {
                if(element instanceof GenericBonus) ((GenericBonus)element).setVisible(true);
                if(element instanceof Grass) ((Grass) element).setBrokenBlock(false);
                if(element instanceof Spike) ((Spike) element).setActive(true);
            }
        }
        camera.setX(0);
    }

    /**
     * Metodo che si occupa di disegnare le varie cose
     * @param graphics graphics2D
     */
    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        if(player.isVisible()){
            opponent.draw(graphics2D);
            player.draw(graphics2D);
        }
        for(ArrayList<Object> strato : mappa){
            for(Object element : strato) {
                if(element instanceof Block)
                    ((Block)element).draw(graphics2D);
                else if(element instanceof GenericBonus)
                    ((GenericBonus)element).draw(graphics2D);
                else if(element instanceof GenericElement)
                    ((GenericElement) element).draw(graphics2D);
            }
        }
        graphics2D.setFont(new Font("Arial", Font.BOLD, 18));
        if(player.isVisible()) {
            graphics2D.setColor(Color.DARK_GRAY);
            String stringLevel = "Livello: " + player.getLevel();
            String stringScore = "Score: " + player.getScore();
            String stringScudo = "Scudo: " + (player.hasShield() ? "SI" : "NO") + " " +
                    (player.getNumShield() > 1 ? "x" + player.getNumShield() : "");

            int max = stringScudo.length();
            graphics2D.drawString(stringLevel, 600 - max * 10 - 10, 30);
            graphics2D.drawString(stringScudo, 600 - max * 10 - 10, 50);
            graphics2D.drawString(stringScore, 600 - max * 10 - 10, 70);
        } else {
            Horse horse = new Horse(240, 145);
            horse.draw(graphics2D);
            graphics2D.setColor(new Color(204, 119, 34, 70));
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.drawRect(150, 200, 300, 300);
            graphics2D.setColor(new Color(204, 119, 34, 30));
            graphics2D.fillRect(150, 200, 300, 300);
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.drawString("CLASSIFICA: ", 250, 300);
            graphics2D.drawString("TU: " + player.getTimer().getSecondi() + " secondi", 200, 370);
            graphics2D.drawString("AVVERSARIO: " +
                    (opponent.getTempoPartita() > 0 ? opponent.getTempoPartita() + " secondi": "//"), 200, 420);
        }
    }

    /**
     * Metodo che si occupa di caricare una mappa
     * @param lvl livello da caricare
     */
    public void loadMap(int lvl){
        try {
            mappa = mapReader.read(String.valueOf(lvl));
        } catch (FileNotFoundException e){
            // player.setLevel(1);
            // loadMap(player.getLevel());
            mappa.clear();
            player.setVisible(false);
            player.getTimer().stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> getMappa() {
        return mappa;
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }

    public Audio getAudio() {
        return audio;
    }
}
