package it.fdb.gocarrot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeysManager extends KeyAdapter {
    private Board board;

    public KeysManager(Board board) {
        this.board = board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        board.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        board.keyReleased(e);
    }
}
