package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.Timer;

import java.util.ArrayList;

/**
 * Creates a seperate thread for managing actuators, and update them when they change.
 * */
public class ActuatorManager implements Runnable {
    private Thread t;
    ArrayList<BaseActuator> actuators;
    Timer timer;

    public ActuatorManager(){
        timer = new Timer();
        actuators = new ArrayList<>();

    }

    public void addActuators(ArrayList a){
        actuators.addAll(a);
    }

    public void start(){
        timer.reset();
        if (t==null){
            t = new Thread (this, "ActuatorManager");
            t.start();
        }
    }

    @Override
    public void run() {
        while(!Globals.opMode.isStopRequested()){
            for (BaseActuator a : actuators){
                if (a.hasChanged()){
                    a.update();
                    if (Globals.DEBUG_ACTUATOR)Log.d("ActuatorManager", "Updated " + a.toString());
                }

            }
            if (Globals.DEBUG_ACTUATOR)Log.d("ActuatorManager", "time: " + timer.timeElapsed());
            timer.reset();
        }

    }
}
