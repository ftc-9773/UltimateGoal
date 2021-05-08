
package org.firstinspires.ftc.teamcode.Utilities;

import android.util.Log;

/**
 * Simple class that executes onConditionMet() the first time that condition() returns true. Alternatively, it runs until the condition is met.
 * To use:
 * new Serialiser(name="optional"){
 *      @Override
 *      public void condition(){return CONDITION_IS_TRUE?}
 *      @Override
 *      public void onConditionMet(){what you want it to do when the condition is met}
 * }
 *
 * All Serialisers are stopped without running onComplete() if the opmode is stopped.
 * */
public abstract class Serialiser extends Threading implements Runnable {
    private Thread t;
    private String name = "";
    boolean reverse = false;
    boolean interrupted = false;

    public Serialiser() {
    }
    public Serialiser(boolean reverse){ this.reverse = reverse; }
    public Serialiser(String name){
        this.name = name;
    }

    public String name(){
        return name;
    }
    public Serialiser(boolean reverse, String name) { this.reverse = reverse; this.name = name; }


    public String getDebugInfo(){
        return t.getState().toString();
    }

    public void start(){
        if(t == null){
            t = new Thread(this, "Seraliser " + name);
            t.start();
            Log.d("SERIALISER " + name, "Started thread");
        }
    }

    @Override
    public void run() {
        while (!condition()) {
            if (_interruptCondition() || interrupted){
                Log.d("SERIALISER " + name, "Interrupted");
                return;
            }
            if (reverse) onConditionMet();
            if (!reverse) during();
            //Log.d("SERIALISER " + name,  "condition not met");
        }
        Log.d("SERIALISER " + name, "condition met");
        if (!reverse) onConditionMet();
        Log.d("SERALISER " + name, "Ended sucessfully" );
    }

    public abstract boolean condition();

    public void during(){

    }

    public abstract void onConditionMet();

    //Automatically stop all threads if the opmode is stopped.
    private boolean _interruptCondition(){
        return Globals.opMode.isStopRequested() || !Globals.opMode.opModeIsActive() || interruptCondition();
    }

    //To be overriden by subclasses if necessary
    public boolean interruptCondition(){
        return false;
    }

    public void interrupt(){
        interrupted = true;
    }

}