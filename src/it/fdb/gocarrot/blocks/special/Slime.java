package it.fdb.gocarrot.blocks.special;

import it.fdb.gocarrot.blocks.Block;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Slime extends Block {
    public Slime(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#00b300"));
        graphics2D.fillRect(x,y, WIDTH, HEIGHT);
        graphics2D.setColor(Color.decode("#008000"));
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawOval(x+5,y+10,2,2);
        graphics2D.drawOval(x+20,y+40,5,5);
        graphics2D.drawOval(x+35,y+15,3,3);
        graphics2D.drawOval(x+45,y+25,2,2);
        graphics2D.drawOval(x+10,y+35,2,2);
        graphics2D.drawOval(x+42,y+47,2,2);
    }
}
