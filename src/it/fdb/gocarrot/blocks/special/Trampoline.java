package it.fdb.gocarrot.blocks.special;

import it.fdb.gocarrot.blocks.Block;

import java.awt.*;

public class Trampoline extends Block {
    public Trampoline(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#1a53ff"));
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.fillRect(x,y, WIDTH,10);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawLine(x+10,y+10,x+5,y+ HEIGHT);
        graphics2D.drawLine(x+40,y+10,x+45,y+ HEIGHT);
    }
}
