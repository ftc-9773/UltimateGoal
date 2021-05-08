package org.firstinspires.ftc.teamcode.Utilities;

public class Timer {
    long startTime;
    long duration = 0;

    public Timer(long ms){
        duration = ms;
    }

    public Timer(){
        startTime = System.currentTimeMillis();
    }

    public long timeElapsed(){
        return System.currentTimeMillis() - startTime;
    }

    public void reset(){
        startTime = System.currentTimeMillis();
    }

    public boolean isDone(){
        return startTime + duration < System.currentTimeMillis();
    }
}
