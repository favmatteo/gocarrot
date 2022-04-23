package it.fdb.gocarrot;

import it.fdb.gocarrot.blocks.Block;
import it.fdb.gocarrot.blocks.gravityblocks.Sand;
import it.fdb.gocarrot.blocks.simple.Grass;
import it.fdb.gocarrot.blocks.special.Spike;
import it.fdb.gocarrot.bonus.GenericBonus;
import it.fdb.gocarrot.element.GenericElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel {

    private final Player player;
    private final Timer timer;
    private Opponent opponent;
    private ArrayList<ArrayList<Object>> mappa;
    private final Camera camera;
    private final MapReader mapReader;

    public Board() {
        player = new Player(this, 100, 480);
        opponent = new Opponent(this, player, 100, 480);
        timer = new Timer();
        mapReader = new MapReader(this);
        loadMap(player.getLevel());

        camera = new Camera();
        setBackground(Color.decode("#87CEFF"));

        opponent.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Toolkit.getDefaultToolkit().sync(); // su linux
                player.setSpeed();
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
        }, 0, 17);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // virtual key
        if(key == KeyEvent.VK_LEFT) player.setKeyLEFT(true);
        if(key == KeyEvent.VK_RIGHT) player.setKeyRIGHT(true);
        if(key == KeyEvent.VK_UP) player.setKeyUP(true);
        if(key == KeyEvent.VK_DOWN) player.setKeyDOWN(true);
    }


    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode(); // virtual key
        if(key == KeyEvent.VK_LEFT) player.setKeyLEFT(false);
        if(key == KeyEvent.VK_RIGHT) player.setKeyRIGHT(false);
        if(key == KeyEvent.VK_UP) player.setKeyUP(false);
        if(key == KeyEvent.VK_DOWN) player.setKeyDOWN(false);
    }

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

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        player.draw(graphics2D);
        opponent.draw(graphics2D);
        // opponent.draw(graphics2D);
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
        String stringLevel = "Livello: " + player.getLevel();
        String stringScore = "Score: " + player.getScore();
        String stringScudo = "Scudo: " + (player.hasShield() ? "SI" : "NO") + " " +
                (player.getNumShield() > 1 ? "x" + player.getNumShield() : "");

        // System.out.println(600 - stringLevel.length()); // 590
        // System.out.println(600 - stringScore.length()); // 592
        // System.out.println(600 - stringScudo.length()); // 590

        int max = stringScudo.length();

        graphics2D.drawString(stringLevel, 600 - max * 10 - 10, 30);
        graphics2D.drawString(stringScudo, 600 - max * 10 - 10, 50);
        graphics2D.drawString(stringScore, 600 - max * 10 - 10, 70);
    }

    public ArrayList<ArrayList<Object>> getMappa() {
        return mappa;
    }

    public void loadMap(int lvl){
        try {
            mappa = mapReader.read(String.valueOf(lvl));
        } catch (FileNotFoundException e){
            player.setLevel(1);
            loadMap(player.getLevel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }


}
