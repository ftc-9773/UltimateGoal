package org.firstinspires.ftc.teamcode.Utilities;

//Takes an action when completed
//Easy to extend Timer that runs in another thread.
public abstract class EggTimer implements Runnable{
    long endTime;
    private Thread t;

    //Length in seconds
    public EggTimer(){}

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
        }
        onComplete();
    }

    public abstract void onComplete();
}
