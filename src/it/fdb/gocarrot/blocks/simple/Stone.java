package it.fdb.gocarrot.blocks.simple;

import it.fdb.gocarrot.blocks.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Stone extends Block {

    private Random random;
    private ArrayList<Integer> valori;

    public Stone(int x, int y) {
        super(x, y);
        valori = new ArrayList<>();
        random = new Random();
        setValori();
    }

    public void setValori(){
        for(int i = 0; i < 13; ++i) {
            valori.add(random.nextInt(-10, 10));
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#918E85"));
        graphics2D.fillRect(x, y, WIDTH, HEIGHT);
        graphics2D.setColor(Color.decode("#999999"));
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawPolyline(new int[]{x+1,x+10,x+15,x+30+valori.get(0),x+35+valori.get(1)},new int[]{y+20+valori.get(2),y+20+valori.get(3),y+10+valori.get(4),y+35+valori.get(5),y+30+valori.get(6)},5);
        graphics2D.drawLine(x+30+valori.get(0),y+35+valori.get(5),x+40,y+35);
        graphics2D.drawPolyline(new int[]{x+49,x+40,x+30},new int[]{y+10+valori.get(7),y+10+valori.get(8),y+15+valori.get(9)},3);
        graphics2D.drawPolyline(new int[]{x+1,x+10,x+20},new int[]{y+39+valori.get(10),y+35+valori.get(11),y+37+valori.get(12)},3);
    }
}
