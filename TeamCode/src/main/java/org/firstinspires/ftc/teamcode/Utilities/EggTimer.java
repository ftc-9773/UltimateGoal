package org.firstinspires.ftc.teamcode.Utilities;

/**
 * Simple class that executes onComplete() after a set amount of time. Alternatively, loops a command for a length of time.
 * To use:
 * new EggTimer(){
 *      @Override
 *      public void getLen(){return TIME_IN_MS}
 *      @Override
 *      public void onComplete(){what you want it to do when the timers up}
 * }
 *
 * All EggTimers are stopped without running onComplete() if the opmode is stopped.
 * */
public abstract class EggTimer implements Runnable{
    long endTime;
    private Thread t;
    boolean reverse = false;

    public EggTimer(){}

    public EggTimer(boolean stop){
        reverse = stop;
    }

    public abstract long getLen();

    public void start(){
        endTime = System.currentTimeMillis() + getLen();
        t = new Thread(this, "EggTimer");
        t.start();
    }

    public boolean isDone(){
        return System.currentTimeMillis() > endTime;
    }

    @Override
    public void run() {
        while (!isDone()){
            if (Globals.opMode.isStopRequested()){
                return; //Interrupt without finishing if opmode is stopped.
            }
            if (reverse) onComplete();

        }
        if (!reverse) onComplete();
    }

    public abstract void onComplete();
}
