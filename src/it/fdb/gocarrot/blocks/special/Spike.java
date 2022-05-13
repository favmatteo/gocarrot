package it.fdb.gocarrot.blocks.special;

import it.fdb.gocarrot.blocks.Block;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Spike extends Block {

    private boolean active;

    public Spike(int x, int y) {
        super(x, y);
        active = true;
        hitBox = new Rectangle(x, y, WIDTH - 1, HEIGHT);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(2));
        if(active) {
            graphics2D.setColor(Color.decode("#333333"));
            graphics2D.fillRect(x, y + 40, WIDTH, 10);
            graphics2D.drawLine(x, y + 40, x, y);
            graphics2D.drawLine(x + 10, y + 40, x + 10, y);
            graphics2D.drawLine(x + 20, y + 40, x + 20, y);
            graphics2D.drawLine(x + 30, y + 40, x + 30, y);
            graphics2D.drawLine(x + 40, y + 40, x + 40, y);
            graphics2D.drawLine(x + 50, y + 40, x + 50, y);
        }else{
            graphics2D.setColor(Color.decode("#A47449"));
            graphics2D.fillRect(x, y - 1, WIDTH, 5);
            graphics2D.setColor(Color.decode("#333333"));
            graphics2D.fillRect(x, y + 40, WIDTH, 10);
            graphics2D.drawLine(x, y + 40, x, y);
            graphics2D.drawLine(x + 10, y + 40, x + 10, y);
            graphics2D.drawLine(x + 20, y + 40, x + 20, y);
            graphics2D.drawLine(x + 30, y + 40, x + 30, y);
            graphics2D.drawLine(x + 40, y + 40, x + 40, y);
            graphics2D.drawLine(x + 50, y + 40, x + 50, y);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
