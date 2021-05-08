package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class Encoder extends BaseActuator{
    public DcMotor encoder;
    String TAG = "ENCODER ";
    private long pos;
    private double vel;
    public ArrayList<Long> posQueue = new ArrayList<>();
    public ArrayList<Long> timequeue = new ArrayList<>();
    //public int smoothing = 10;
    private long last_pos;
    private long last_time;
    private long consecutive_errors = 0;
    private static final int COUNTS_PER_REVOLUTION = 8192;
    long minDT = 5;


    public Encoder(String name){
        encoder = Globals.hardwareMap.get(DcMotorEx.class, name);
        TAG = TAG + name + " PORT " + encoder.getPortNumber();
        //encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
            if (time - last_time > minDT) {
                pos = encoder.getCurrentPosition();
                //timequeue.add(time);
                //if (timequeue.size() > smoothing) timequeue.remove(0);
                //posQueue.add(pos);
                //if (posQueue.size() > smoothing) posQueue.remove(0);
                vel = ((double) (pos - last_pos)) / (time / 1000. - last_time/ 1000.) / COUNTS_PER_REVOLUTION * Math.PI * 2; //rad/s
                //vel = (double) (pos - last_pos) / (time - last_time) * 1000;
                //Log.d("Encoder", "Change in ticks " + (pos - last_pos) + "Change in time: " + (time - last_time));
                last_pos = pos;
                last_time = time;

                if (-vel < -1000000) {
                    Log.d("AHHH", "lt " + last_time + " time: " + time + " vel " + vel + " lp " + last_pos + " pos " + pos);
                }
            }
        }
    }

    //Radians / s
    public double getVel(){
        update();
        return -vel;
    }

    public void reset(){
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public String toString(){
        return TAG;
    }
}
