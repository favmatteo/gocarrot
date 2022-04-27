package it.fdb.gocarrot;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {
    public MainFrame() {
        Board board = new Board();
        board.setLocation(0, 0);
        board.setSize(this.getSize());
        board.setVisible(true);
        this.add(board);

        addKeyListener(new KeysManager(board));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    board.getPlayer().getClient().close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }
}
