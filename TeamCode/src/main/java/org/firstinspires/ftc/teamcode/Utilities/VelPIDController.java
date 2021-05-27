package org.firstinspires.ftc.teamcode.Utilities;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Vikesh on 11/20/2017.
 */

public class VelPIDController {
    public double KP;
    public double KI;
    public double KD;
    private double integral;
    public double derivative;
    public double prevError;
    private double output;
    private long lastTime;
    private long deltaTime;
    private boolean firstRun = true;
    public String name = "PID";

    private boolean useExponential;

    private static int maxDeltaTime = 800;
    private static boolean DEBUG = true;

    public VelPIDController(double KP, double KI, double KD) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
        useExponential = false;
        if (DEBUG) {
            Log.i("PID", "KP: " + KP);
            Log.i("PID", "KI: " + KI);
            Log.i("PID", "KD: " + KD);
        }
    }

    public double getPIDCorrection(double error, double derivative, double decay){
        return 0;
    }


    public double getPIDCorrection(double error) {
        // calculate helper variables
        deltaTime = System.currentTimeMillis() - lastTime;
        // If it is the first run, just return proportional error as i and d cannot be cauculated yet
        if (firstRun || deltaTime > maxDeltaTime) {
            firstRun = false;
            output =  error * KP;
        } else if (deltaTime == 0){
            output = error * KP + integral * KI;
            Log.d(name + "Error", "DT = 0");
        } else {
            // Calculate I and D errors
            integral = 0.9 * integral + (error * deltaTime);
            derivative = (error - prevError) / deltaTime;
            if (DEBUG){
                Log.i("PID-", "I : "+ integral);
                Log.d("PID", "D : "+ derivative );
                Log.d(name, "," + System.currentTimeMillis() + "," + error + "," + derivative);
            }
            output = KP * error + KI * integral + KD * derivative;
        }
        // Set previous values for next time

        lastTime = System.currentTimeMillis();
        prevError = error;

        if (DEBUG) Log.d("P.I.D", "Got PID Correction. First run: " + firstRun + " delta time: " + deltaTime);
        return output;
    }

    public double getPIDCorrection(double target, double actual) {
        double correction =  getPIDCorrection(target - actual);

        return correction;
    }
    public void resetPID(){
        this.prevError = 0;
        this.lastTime = System.currentTimeMillis() - 800;
    }
}