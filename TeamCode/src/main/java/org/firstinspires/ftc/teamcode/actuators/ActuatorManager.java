package org.firstinspires.ftc.teamcode.actuators;

import android.accounts.AccountManagerFuture;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Create a seperate thread for actuators, and update them when they change.
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
        for (BaseActuator a : ActuatorStorage.actuators){
            if (a.hasChanged()){}
        }

    }
}
