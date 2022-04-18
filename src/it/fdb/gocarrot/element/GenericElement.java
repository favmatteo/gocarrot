package it.fdb.gocarrot.element;

import it.fdb.gocarrot.Camera;

import java.awt.*;

public abstract class GenericElement {
    protected int x;
    protected int y;
    protected Rectangle hitBox;

    protected int startX;

    public static final  int WIDTH = 50;
    public static final int HEIGHT = 50;


    public GenericElement(int x, int y) {
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
}
