package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.teamcode.Components.actuators.Encoder;
import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Components.actuators.SimpleMotor;
import org.firstinspires.ftc.teamcode.Utilities.EggTimer;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

import java.sql.Time;

import static org.firstinspires.ftc.teamcode.Utilities.Globals.DEBUG_LAUNCHER;

public class Launcher {
    public static String DEBUG_TAG = "Launcher";
    static int failed_launches_called = 0;
    Serialiser launchTask = null;
    Serialiser velocityController = null;
    boolean motorsOn = false;
    public enum MODE {POLE, BASKET}
    public MODE mode = MODE.BASKET;
    public PIDController velPID;
    boolean forceLaunch = false;
    public Motor cMotor0;
    public Motor cMotor1;
    public Encoder encoder;

    int isLaunch = 0;
    Servo flickServo;
    Timer timer = new Timer();
    Timer otherTimer = new Timer();

    double lastVel = -1, Vel;
    double lastTime = System.currentTimeMillis();

    public double motorPower = 0, motorBasePower;

    double baseMotorPower, basePolePower;
    double flickOpenPos, flickClosePos;
    double motorSpeed, poleSpeed, targetSpeed, motorWindow;


    public Launcher(){
        cMotor0 = new Motor("launchMotor0");
        cMotor1 = new Motor("launchMotor1");
        encoder = new Encoder("launchMotor0");
        flickServo = new Servo("launchServo");

        JsonReader reader = new JsonReader("componentJson");
        flickOpenPos = reader.getDouble("launchOpenPos",0.60); //trial and error
        flickClosePos = reader.getDouble("launchClosePos", 0.99); //trial and error
        motorSpeed = reader.getDouble("launchMotorSpeed",23); //trial and error
        poleSpeed = reader.getDouble("launchPoleSpeed", 20);
        motorWindow = reader.getDouble("motorWindow", 0.2);
        baseMotorPower = reader.getDouble("launchMotorBasePower", 0.77);
        basePolePower = reader.getDouble("launchMotorBasePower", 0.75);
        motorBasePower = reader.getDouble("launchMotorBasePower", 0.75);
        velPID = new PIDController(reader.getDouble("lkp"), reader.getDouble("lki"), reader.getDouble("lkd"));
        velPID.name = "VELPID";
        targetSpeed = motorSpeed;
        flickServo.setPosition(flickClosePos);
    }



    public void motorOn(){
        motorsOn = true;
        Log.d(DEBUG_TAG, "Turning on motors");
        final double minCycleTime = 10;
        if (velocityController == null){
            otherTimer.reset();
            timer.reset();
            velPID.resetPID();
            Log.d(DEBUG_TAG, "velocityController didn't exist");
            velocityController = new Serialiser() {
                @Override
                public boolean condition() {
                    return !motorsOn;
                }
                @Override
                public void during(){
                    if (!(timer.timeElapsed() < minCycleTime)){
                        double vel = encoder.getVel();
                        double correction = velPID.getPIDCorrection(targetSpeed, vel);
                        if (correction > 0.15){
                            correction = 0.15;
                        } else if (correction < -0.15){
                            correction = -0.15;
                        }
                        if (Double.isNaN(correction)){
                            velPID.resetPID();
                            return;
                        }
                        Log.d("VelPID", "," + otherTimer.timeElapsed() + "," + velPID.prevError + "," + velPID.derivative + "," + isLaunch);
                        if (isLaunch == 1) isLaunch = 0;
                        Log.d("VelocityController", "Base power: " + motorBasePower + " made correction " + correction);
                        motorPower = motorBasePower + correction;
                        if (motorPower > 1) motorPower = 1;
                        if (motorPower < -1) motorPower = -1;
                        cMotor0.setPower(motorPower);
                        cMotor1.setPower(-motorPower);
                        timer.reset();
                    }
                }
                @Override
                public void onConditionMet() {
                    motorsOn = false;
                    cMotor0.setPower(0);
                    cMotor1.setPower(0);
                    motorPower = 0;
                    velocityController = null;
                    Log.d("VelocityController", "Stopped");
                }
            };
            velocityController.start();
            Log.d(DEBUG_TAG, "Created Velocity Controller");
        }
    }

    public double getMotorSpeed(){
        return encoder.getVel();
    }

    public void forceLaunch(){
        forceLaunch = true;
    }

    public void setMode(MODE mode){
        if (mode == this.mode){
            return;
        }
        this.mode = mode;
        if (mode == MODE.BASKET) {
            targetSpeed = motorSpeed;
            motorBasePower = baseMotorPower;
        }
        if (mode == MODE.POLE) {
            targetSpeed = poleSpeed;
            motorBasePower = basePolePower;
        }
    }

    public void motorOff(){
        if (launchTask != null) launchTask.interrupt(); //cancel attempted launches if user intervenes
        motorsOn = false;
    }

    public double abs(double v) {return v > 0? v : -v;}

    public boolean motorsAtSpeed(){
        Vel = encoder.getVel();
        boolean isGreaterThanMin = targetSpeed - motorWindow < Vel;
        boolean isLessThanMax = targetSpeed + 1  * motorWindow > Vel;
        boolean result = isGreaterThanMin && isLessThanMax;
        double a;
        if (lastVel == -1){
            a = 100000000;
        } else {
            a = (Vel - lastVel) / (System.currentTimeMillis() - lastTime);
        }
        lastVel = Vel;
        lastTime = System.currentTimeMillis();
        //Log.d(DEBUG_TAG, "a: " + a);
//        Log.d(DEBUG_TAG, "Motor speeds are as follows: " + cMotor0.getVelocity());
//        Log.d(DEBUG_TAG, "Motor should be at speed " + motorSpeed + " returning " + result);
        return result;
    }

    public void launchDisk(){
        // Wait until motor is at speed, and then launch the disk.
        if (launchTask != null) {
            Log.d(DEBUG_TAG, "Did Not start new launch, existed launcher " + launchTask.name());
            return;
        } //Just so there's never a bunch of invisible threads in the background
        motorOn();
        launchTask = new Serialiser("Launch " + failed_launches_called) {
            @Override
            public boolean condition() {
                    return motorsAtSpeed() || forceLaunch;
                }
                @Override
                public void onConditionMet() {
                    Log.d(DEBUG_TAG, "Launching disk at speed " + encoder.getVel());
                    forceLaunch = false;
                    isLaunch = 1;
                    flick();
                }
            };
        launchTask.start();
//            if (DEBUG_LAUNCHER){
//                Log.i(DEBUG_TAG, "Failed to launch disk, motor not at speed. Launches failed: " + failed_launches_called);
//                Log.i(DEBUG_TAG, "Seraliser State: " + launchTask.getDebugInfo());
//            }
    }

    public void flick(){
        flickServo.setPosition(flickOpenPos);
        new EggTimer(){
            @Override
            public long getLen() {
                return 250;
            } //ms
            @Override
            public void onComplete() {
                flickServo.setPosition(flickClosePos);
                new EggTimer(){
                    @Override
                    public long getLen() {
                        return 250;
                    }

                    @Override
                    public void onComplete() {
                        launchTask = null;
                    }
                }.start();
            }
        }.start();
    }

    public boolean getLaunchTaskState(){
        return launchTask != null;
    }
    public void cancelLaunchDisk(){
        if (launchTask != null) {
            launchTask.interrupt();
            launchTask = null;
            if (DEBUG_LAUNCHER) Log.d(DEBUG_TAG, "launch cancelled");
        }
    }
}
