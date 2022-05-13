package it.fdb.gocarrot.element;

import java.awt.*;
import java.awt.geom.Line2D;

public class Horse extends GenericElement{
    public Horse(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        // cavallo
        graphics2D.setColor(Color.decode("#662200"));
        graphics2D.fillRoundRect(x-25, y-55, 150, 55, 70, 70);
        // zampe
        graphics2D.setStroke(new BasicStroke(8));
        graphics2D.draw(new Line2D.Float(x, y, x, y+50));
        graphics2D.draw(new Line2D.Float(x+25, y, x+25, y+50));
        graphics2D.draw(new Line2D.Float(x+80, y, x+80, y+50));
        graphics2D.draw(new Line2D.Float(x+105, y, x+105, y+50));
        // cosce
        graphics2D.fillOval(x+55,y-55,70,65);
        graphics2D.fillPolygon(new Polygon(new int[]{x+25,x+80,x+85},new int[]{y,y,y+10}, 3));
        graphics2D.fillOval(x-20,y-45,70,50);
        graphics2D.fillPolygon(new Polygon(new int[]{x+75,x+130,x+108},new int[]{y-20,y-20,y+25}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{x+50,x+105,x+83},new int[]{y-20,y-20,y+25}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{x-24,x+30,x-2},new int[]{y-20,y-20,y+25}, 3));
        graphics2D.fillPolygon(new Polygon(new int[]{x+10,x+55,x+23},new int[]{y-20,y-20,y+25}, 3));
        // coda
        graphics2D.setColor(Color.decode("#4d1a00"));
        graphics2D.fillPolygon(new Polygon(new int[]{x+118,x+130,x+145},new int[]{y-45,y+15,y+5}, 3));
        // collo
        graphics2D.setColor(Color.decode("#662200"));
        graphics2D.fillRoundRect(x-25, y-90, 45, 90, 70, 70);
        // testa con mandibola
        graphics2D.fillRoundRect(x-60, y-100, 70, 30, 70, 70);
        graphics2D.fillRoundRect(x-60, y-70, 70, 15, 70, 70);
        // orecchie
        graphics2D.fillPolygon(new Polygon(new int[]{x+15,x-15,x-5},new int[]{y-60,y-60,y-120}, 3));
        // occhio
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(x-20,y-100,15,15);
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(x-20,y-100,9,9);
        // dentini
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(x-48,y-72,3,5);
        graphics2D.fillRect(x-44,y-72,3,5);
        graphics2D.fillRect(x-40,y-72,3,5);
        graphics2D.fillRect(x-35,y-72,3,5);
        // criniera
        graphics2D.setColor(Color.decode("#4d1a00"));
        graphics2D.fillPolygon(new Polygon(new int[]{x+3,x+15,x+35},new int[]{y-98,y-40,y-40}, 3));
    }
}
