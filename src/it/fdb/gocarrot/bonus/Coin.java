package it.fdb.gocarrot.bonus;

import java.awt.*;

public class Coin extends GenericBonus{

    private static final int WIDTH = 36;
    private static final int HEIGHT = 36;

    public Coin(int x, int y) {
        super(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if(isVisible()) {
            graphics2D.setColor(Color.decode("#e9ad03"));
            graphics2D.fillOval(x + 7, y + 7, WIDTH, HEIGHT);
            graphics2D.setColor(Color.decode("#dba300"));
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawOval(x + 11,y + 11,28,28);
            graphics2D.setFont(new Font("Arial", Font.BOLD, 20));
            graphics2D.drawString("$",x + 19, y + 32);
        }
    }
}
