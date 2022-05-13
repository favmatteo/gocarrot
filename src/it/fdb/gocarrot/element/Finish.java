package it.fdb.gocarrot.element;

import java.awt.Color;
import java.awt.Graphics2D;

public class Finish extends GenericElement{

    public Finish(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        boolean firstBlack = true;
        for(int i = 0; i < 5; ++i) {
            for(int j = 0; j < 5; ++j) {
                if(firstBlack) graphics2D.setColor(Color.BLACK);
                else graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(x + (j * 10), y + (i * 10), 10, 10);
                firstBlack = !firstBlack;
            }
        }

    }
}
