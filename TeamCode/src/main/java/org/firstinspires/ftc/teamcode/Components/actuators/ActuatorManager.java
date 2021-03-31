package org.firstinspires.ftc.teamcode.Components.actuators;

import org.firstinspires.ftc.teamcode.Utilities.Globals;

/**
 * Creates a seperate thread for managing actuators, and update them when they change.
 * */
public class ActuatorManager implements Runnable {
    private Thread t;

    public ActuatorManager(){

    }

    public void start(){
        if (t==null){
            t = new Thread (this, "ActuatorManager");
            t.start();
        }
    }

    @Override
    public void run() {
        while(!Globals.opMode.isStopRequested()){
            synchronized (this){
                for (BaseActuator a : ActuatorStorage.actuators){
                    if (a.hasChanged()){
                            a.update();
                    }
                }
            }
        }

    }
}
