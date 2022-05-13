package it.fdb.gocarrot.blocks.simple;

import it.fdb.gocarrot.blocks.Block;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Air extends Block {
    public Air(int x, int y) {
        super(x, y);
        hitBox = new Rectangle(x, y, 0, 0);
    }

    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
