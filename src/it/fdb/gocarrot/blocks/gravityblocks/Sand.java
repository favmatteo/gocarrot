package it.fdb.gocarrot.blocks.gravityblocks;

import it.fdb.gocarrot.Board;
import it.fdb.gocarrot.blocks.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Sand extends Block implements IGravity{
    private Random random;
    private ArrayList<ArrayList<Integer>> randomBlock = new ArrayList<>();
    private Board board;

    private float ySpeed;

    public Sand(int x, int y, Board board) {
        super(x, y);
        random = new Random();
        generateRandomBlock();
        this.board = board;
    }

    // genera i pallini più scuri casualmente su ogni blocco
    public void generateRandomBlock(){
        for(int i = 0; i < 5; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(random.nextInt(40));
            tmp.add(random.nextInt(40));
            randomBlock.add(tmp);
        }
    }

    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(Color.decode("#C2B280"));
        graphics2D.fillRect(x, y, WIDTH, HEIGHT);
        graphics2D.setColor(Color.decode("#b4a064"));
        for(ArrayList<Integer> element : randomBlock){
            graphics2D.fillRect(x + element.get(0), y + 5 + element.get(1), 5, 5);
        }
    }

    public void gravity(){
        ySpeed += gravityVal;

        // collisione verticale
        hitBox.y += ySpeed + 1;
        for(ArrayList<Object> strato : board.getMappa()){
            for(Object block : strato) {
                if(block instanceof Block) {
                    if (hitBox.intersects(((Block) block).getHitBox()) && this != block) {
                        hitBox.y -= ySpeed;
                        while (!((Block) block).getHitBox().intersects(hitBox)) {
                            hitBox.y += Math.signum(ySpeed);
                        }
                        hitBox.y += Math.signum(ySpeed);
                        ySpeed = 0;
                        y = hitBox.y;
                    }
                }
            }
        }


        y += ySpeed - 1;

        hitBox.x = x;
        hitBox.y = y;
    }
}
