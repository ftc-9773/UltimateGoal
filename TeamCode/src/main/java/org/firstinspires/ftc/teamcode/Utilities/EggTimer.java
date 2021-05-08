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
public abstract class EggTimer extends Threading implements Runnable{
    long endTime;
    private Thread t;
    boolean reverse = false;
    boolean interrupted;

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

    //Automatically stop all threads if the opmode is stopped.
    private boolean _interruptCondition(){
        return Globals.opMode.isStopRequested() || !Globals.opMode.opModeIsActive() || interruptCondition();
    }

    //To be overriden by subclasses if necessary
    public boolean interruptCondition(){
        return interrupted;
    }

    public void interrupt(){
        interrupted = true;
    }

    public void during(){

    }

    @Override
    public void run() {
        while (!isDone()){
            if (_interruptCondition()){
                return; //Interrupt without finishing if opmode is stopped or something goes wrong.
            }
            if (reverse) onComplete();
            if (!reverse) during();
        }
        if (!reverse) onComplete();
    }

    public abstract void onComplete();
}
