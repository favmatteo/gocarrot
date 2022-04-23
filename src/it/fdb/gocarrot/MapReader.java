package it.fdb.gocarrot;

import it.fdb.gocarrot.blocks.Block;
import it.fdb.gocarrot.blocks.gravityblocks.Sand;
import it.fdb.gocarrot.blocks.simple.*;
import it.fdb.gocarrot.blocks.special.*;
import it.fdb.gocarrot.bonus.Coin;
import it.fdb.gocarrot.bonus.Shield;
import it.fdb.gocarrot.element.Finish;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapReader {
    private final ArrayList<ArrayList<Object>> mappa;
    private final Board board;

    public MapReader(Board board) {
        this.mappa = new ArrayList<>();
        this.board = board;
    }

    public ArrayList<ArrayList<Object>> read(String level) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/mappa" + level));
        String line = reader.readLine();
        int x = 0;
        int y = 0;
        while (line != null){
            ArrayList<Object> strato = new ArrayList<>();
            if(!line.startsWith("//")) {
                for(int i = 0; i < line.length(); ++i){
                    char c = line.charAt(i);
                    switch (c) {
                        case '|' -> strato.add(new Brick(x, y));
                        case '#' -> strato.add(new Grass(x, y));
                        case '*' -> strato.add(new Stone(x, y));
                        case 'S' -> strato.add(new Sand(x, y, this.board));
                        case '^' -> strato.add(new Spike(x, y));
                        case 'o' -> strato.add(new Trampoline(x, y));
                        case '~' -> strato.add(new Slime(x, y));
                        case '°' -> strato.add(new Ice(x, y));
                        case 'c' -> strato.add(new Coin(x, y));
                        case '+' -> strato.add(new Shield(x, y));
                        case 'F' -> strato.add(new Finish(x, y));
                        default -> strato.add(new Air(x, y));
                    }
                    x += Block.WIDTH;
                }
                x = 0;
            }
            mappa.add(strato);
            y += 50;
            line = reader.readLine();
        }
        return mappa;
    }
}
