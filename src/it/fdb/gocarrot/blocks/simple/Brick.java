package it.fdb.gocarrot.blocks.simple;

import it.fdb.gocarrot.blocks.Block;

import java.awt.Color;
import java.awt.Graphics2D;

public class Brick extends Block {

    public Brick(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#999999"));
        graphics2D.fillRect(x,y, WIDTH, HEIGHT);
        graphics2D.setColor(Color.decode("#cb4149"));
        graphics2D.fillRect(x+2,y+2,26,10);
        graphics2D.fillRect(x+30,y+2,18,10);
        graphics2D.fillRect(x+2,y+14,16,10);
        graphics2D.fillRect(x+20,y+14,28,10);
        graphics2D.fillRect(x+2,y+26,26,10);
        graphics2D.fillRect(x+30,y+26,18,10);
        graphics2D.fillRect(x+2,y+38,16,10);
        graphics2D.fillRect(x+20,y+38,28,10);
    }
}
