package it.fdb.gocarrot.bonus;

import it.fdb.gocarrot.Camera;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class GenericBonus {
    protected int x;
    protected int y;
    protected Rectangle hitBox;
    private boolean visible;

    protected int startX;

    public GenericBonus(int x, int y, int width, int height) {
        this.x = x;
        startX = x;
        this.y = y;
        visible = true;
        hitBox = new Rectangle(x, y, width, height);
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
