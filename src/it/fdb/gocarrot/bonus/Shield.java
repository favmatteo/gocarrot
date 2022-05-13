package it.fdb.gocarrot.bonus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Shield extends GenericBonus {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 36;

    public Shield(int x, int y) {
        super(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (isVisible()) {
            graphics2D.setColor(Color.decode("#82766f"));
            graphics2D.fillRect(x + 9, y + 4, 32, 36);
            graphics2D.fillPolygon(new int[]{x + 9, x + 41, x + 25}, new int[]{y + 40, y + 40, y + 45}, 3);
            graphics2D.setColor(Color.decode("#ad7658"));
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.drawLine(x + 10, y + 30, x + 40, y + 30);
            graphics2D.drawLine(x + 25, y + 5, x + 25, y + 43);
        }
    }
}
