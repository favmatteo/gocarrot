package it.fdb.gocarrot;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class App {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setSize(600, 635);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("GO, Carrot");

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
