package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Encoder extends BaseActuator{
    public DcMotor encoder;
    String TAG = "ENCODER ";
    private long pos;
    private double vel;
    private long last_pos;
    private long last_time;
    private long consecutive_errors = 0;
    private static final int COUNTS_PER_REVOLUTION = 8192;


    public Encoder(String name){
        encoder = Globals.hardwareMap.get(DcMotorEx.class, name);
        TAG = TAG + name + " PORT " + encoder.getPortNumber();
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        last_time = System.currentTimeMillis();
        last_pos = encoder.getCurrentPosition();
        reset();
    }

    public long getPos(){
        return pos;
    }

    @Override
    public boolean hasChanged() {
        return true;
    }

    @Override
    public void update(){
        if (encoder != null){
            try {
                pos = encoder.getCurrentPosition();
                consecutive_errors = 0;
            } catch (RuntimeException e) {
                consecutive_errors ++;
                Log.d("ASSERTIONERROR", "caught error " + consecutive_errors, e);
                return;
            }
            long time = System.currentTimeMillis();
            vel = ((double) (pos - last_pos)) / (time / 1000. - last_time / 1000.) / COUNTS_PER_REVOLUTION  * Math.PI * 2; //rad/s
            //vel = (double) (pos - last_pos) / (time - last_time) * 1000;
            //Log.d("Encoder", "Change in ticks " + (pos - last_pos) + "Change in time: " + (time - last_time));
            last_pos = pos;
            last_time = time;
        }
    }

    //Radians / s
    public double getVel(){
        return -vel;
    }

    public void reset(){
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public String toString(){
        return TAG;
    }
}
