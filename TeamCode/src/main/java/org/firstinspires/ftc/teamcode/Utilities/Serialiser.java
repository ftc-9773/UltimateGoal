package org.firstinspires.ftc.teamcode.Utilities;

public abstract class Serialiser implements Runnable {
    private Thread t;

    public Serialiser() {
    }

    public void start(){
        t = new Thread(this, "Seraliser");
        t.run();
    }

    @Override
    public void run() {
        while (!condition()) {
        }
        onConditionMet();

    }

    public abstract boolean condition();

    public abstract void onConditionMet();

}