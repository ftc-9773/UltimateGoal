package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Utilities.EggTimer;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;

public class Launcher {
    public static Boolean DEBUG_THREADING = true;
    public static String DEBUG_TAG = "Launcher";
    static int failed_launches_called = 0;
    Serialiser launchTask = null;

    Motor cMotor0;
    Motor cMotor1;
    Servo flickServo;

    double flickOpenPos, flickClosePos;
    double motorSpeed = 20;

    public Launcher(){
        cMotor0 = new Motor("launchMotor0");
        cMotor1 = new Motor("launchMotor1");
        flickServo = new Servo("launchServo");
        flickOpenPos = 0.87; //trial and error

        flickClosePos = 0.6; //trial and error
        flickServo.setPosition(flickClosePos);
    }

    public void motorOn(){
        cMotor0.setVelocity(-motorSpeed);
        cMotor1.setVelocity(motorSpeed);
    }

    public void motorOff(){
        cMotor0.setVelocity(0);
        cMotor1.setVelocity(0);
    }

    public boolean motorsAtSpeed(){
        boolean result = cMotor0.getVelocity() >motorSpeed;
        Log.d(DEBUG_TAG, "Motor speeds are as follows: " + cMotor0.getVelocity() + " " + cMotor1.getVelocity());
        Log.d(DEBUG_TAG, "Motor should be at speed " + motorSpeed + " returning " + result);
        return result;
    }

    public synchronized void launchDisk(){
        // Wait until motor is at speed, and then launch the disk.
        if (!motorsAtSpeed()){
            failed_launches_called += 1;
            motorOn();
            launchTask = new Serialiser("Launch " + failed_launches_called) {
                @Override
                public boolean condition() {
                    return motorsAtSpeed();
                }
                @Override
                public void onConditionMet() {
                    launchDisk();
                    launchTask = null;
                }
            };
            launchTask.start();
            if (DEBUG_THREADING){
                Log.i(DEBUG_TAG, "Failed to launch disk, motor not at speed. Launches failed: " + failed_launches_called);
                Log.i(DEBUG_TAG, "Seraliser State: " + launchTask.getDebugInfo());
            }
            return;
        }
        launchTask = null;
        flickServo.setPosition(flickOpenPos);
        new EggTimer(){
            @Override
            public long getLen() {
                return 500;
            } //ms
            @Override
            public void onComplete() {
                flickServo.setPosition(flickClosePos);
            }
        }.start();
    }
    public boolean getLaunchTaskState(){
        return launchTask != null;
    }
    public void cancelLaunchDisk(){
        if (launchTask != null) launchTask.interrupt();
    }
}
