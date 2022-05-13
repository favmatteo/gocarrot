package it.fdb.gocarrot.blocks.simple;

import it.fdb.gocarrot.blocks.Block;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class Grass extends Block {
    private final Random random;
    private final ArrayList<ArrayList<Integer>> randomBlock = new ArrayList<>();
    private boolean brokenBlock;


    public Grass(int x, int y) {
        super(x, y);
        random = new Random();
        generateRandomBlock();
    }

    // genera i pallini più scuri casualmente su ogni blocco
    public void generateRandomBlock(){
        for(int i = 0; i < 5; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(random.nextInt(40));
            tmp.add(random.nextInt(40));
            randomBlock.add(tmp);
        }
    }

    public void draw(Graphics2D graphics2D){
        if(!brokenBlock) {
            graphics2D.setColor(Color.decode("#567d46"));
            graphics2D.fillRect(x, y, WIDTH, HEIGHT);
            graphics2D.setColor(Color.decode("#7F644C"));
            graphics2D.fillRect(x, y + 5, WIDTH, HEIGHT - 5);
            graphics2D.setColor(Color.decode("#604b39"));
            for (ArrayList<Integer> element : randomBlock) {
                graphics2D.fillRect(x + element.get(0), y + 5 + element.get(1), 5, 5);
            }
        }else{
            graphics2D.setColor(Color.decode("#567d46"));
            graphics2D.fillRect(x, y, WIDTH, HEIGHT);
            graphics2D.setColor(Color.decode("#7F644C"));
            graphics2D.fillRect(x, y + 5, WIDTH, HEIGHT - 5);
            graphics2D.setColor(Color.decode("#604b39"));
            for (ArrayList<Integer> element : randomBlock) {
                graphics2D.fillRect(x + element.get(0), y + 5 + element.get(1), 5, 5);
            }
            graphics2D.drawPolyline(new int[]{x+25,x+25,x+20,x+20,x+15}, new int[]{y+5,y+15,y+25,y+30,y+35},5);
            graphics2D.drawPolyline(new int[]{x+25,x+30,x+30,x+35}, new int[]{y+15,y+20,y+35,y+40},4);
        }


    }

    public void setBrokenBlock(boolean brokenBlock) {
        this.brokenBlock = brokenBlock;
    }
}
