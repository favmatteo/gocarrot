package it.fdb.gocarrot.blocks;

import it.fdb.gocarrot.Camera;

import java.awt.Rectangle;
import java.awt.Graphics2D;

public abstract class Block {
    protected int x;
    protected int y;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    protected Rectangle hitBox;

    protected int startX;

    public Block(int x, int y) {
        this.x = x;
        startX = x;
        this.y = y;
        hitBox = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public abstract void draw(Graphics2D graphics2D);

    public void set(Camera camera){
        if(camera.getX() <= 0) {
            x = startX + camera.getX();
            hitBox.x = x;
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
