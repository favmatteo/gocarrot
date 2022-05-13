package it.fdb.gocarrot;

public class Timer implements Runnable{
    private int secondi;
    private boolean end;

    public Timer(){
        secondi = 0;
        end = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!end) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!end) secondi++;
        }
    }

    public void stop() {
        end = true;
    }

    public int getSecondi() {
        return secondi;
    }
}
