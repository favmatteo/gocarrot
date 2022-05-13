package it.fdb.gocarrot.blocks.special;

import it.fdb.gocarrot.blocks.Block;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Ice extends Block {
    public Ice(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#b3ffff"));
        graphics2D.fillRect(x,y, WIDTH, HEIGHT);
        graphics2D.setColor(Color.decode("#00ffff"));
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawLine(x+7,y+15,x+15,y+7);
        graphics2D.drawLine(x+5,y+30,x+30,y+5);
        graphics2D.drawLine(x+40,y+45,x+45,y+40);
    }
}
