package it.fdb.gocarrot;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    public MainFrame() {
        Board board = new Board();
        board.setLocation(0, 0);
        board.setSize(this.getSize());
        board.setVisible(true);
        this.add(board);

        addKeyListener(new KeysManager(board));
    }
}
