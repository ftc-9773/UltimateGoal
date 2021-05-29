package org.firstinspires.ftc.teamcode.Components.sensors;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Components.actuators.BaseActuator;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.Timer;

import java.util.ArrayList;

public class Encoder extends BaseActuator {
    public DcMotor encoder;
    String TAG = "ENCODER ";
    private long pos;
    private double vel;
    public ArrayList<Long> posQueue = new ArrayList<>();
    public ArrayList<Long> timequeue = new ArrayList<>();
    public ArrayList<Double> velTracker = new ArrayList<>();
    Boolean log = true;
    public int smoothing = 3;
    private long last_pos;
    private long last_time;
    private long alt_last_time;
    private double alt_v;
    private long consecutive_errors = 0;
    public static final int COUNTS_PER_REVOLUTION_NEVEREST = 28;
    public static final int COUNTS_PER_REVOLUTION_REV_EXTERNAL = 8192;
    enum mode {NEVEREST, REV_E};
    public int COUNTS_PER_REVOLUTION = -1;
    long minDT = 5;
    Timer timerUtil = new Timer();

    public Encoder(String name){
        encoder = Globals.hardwareMap.get(DcMotor.class, name);
        TAG = TAG + name + " PORT " + encoder.getPortNumber();
        //encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        last_time = System.currentTimeMillis();
        alt_last_time = System.currentTimeMillis();
        last_pos = encoder.getCurrentPosition();
        if (COUNTS_PER_REVOLUTION == -1){
            COUNTS_PER_REVOLUTION = COUNTS_PER_REVOLUTION_NEVEREST; //Default
        }
        //reset();
    }
    public Encoder(String name, mode m){
        if (m == mode.NEVEREST){
            COUNTS_PER_REVOLUTION = COUNTS_PER_REVOLUTION_NEVEREST;
        } else if (m == mode.REV_E){
            COUNTS_PER_REVOLUTION = COUNTS_PER_REVOLUTION_REV_EXTERNAL;
        }
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
        long time;
        long alt_time;
        if (encoder != null){
            try {
                timerUtil.reset();
                time = System.currentTimeMillis();
                if (log)Log.d(TAG, "Time Elapsed:" + timerUtil.timeElapsed());
                timerUtil.reset();
                if (log)pos = encoder.getCurrentPosition();
                alt_time = System.currentTimeMillis();
                Log.d(TAG, "getTime Elapsed:" + timerUtil.timeElapsed());
                consecutive_errors = 0;
            } catch (RuntimeException e) {
                consecutive_errors ++;
                time = System.currentTimeMillis();
                Log.d("ASSERTIONERROR", "caught error " + consecutive_errors, e);
                return;
            }


            if (time - last_time > minDT) {
                timequeue.add(time);
                if (timequeue.size() > smoothing) timequeue.remove(0);
                posQueue.add(pos);
                if (posQueue.size() > smoothing) posQueue.remove(0);
                //vel = ((double) (pos - last_pos)) / (time / 1000. - last_time/ 1000.) / COUNTS_PER_REVOLUTION * Math.PI * 2; //rad/s
                vel = 0;
                double dt = 0;
                if (posQueue.size() > 1){
                    vel = posQueue.get(posQueue.size() - 1) - posQueue.get(0);
                } else {
                    vel = posQueue.get(0) - last_pos;
                }
                if (timequeue.size() != 1) {
                    dt  = (timequeue.get(timequeue.size() - 1) - timequeue.get(0) )/ 1000.;
                } else {
                    dt = (timequeue.get(0) - last_time) / 1000.;
                }

                vel = vel / dt / COUNTS_PER_REVOLUTION * Math.PI * 2;
                velTracker.add(vel);
                if (velTracker.size() > 2){
                    velTracker.remove(0);
                }
//                if ((vel > 70 + velTracker.get(0) || vel < velTracker.get(0) - 70) && Math.abs(vel) > 100){
//                    return;
//                }
                alt_v = vel / (alt_time - alt_last_time) / COUNTS_PER_REVOLUTION_NEVEREST * Math.PI * 2 * 1000;
                //vel = (double) (pos - last_pos) / (time - last_time) * 1000;
                //Log.d("Encoder", "Change in ticks " + (pos - last_pos) + "Change in time: " + (time - last_time));

                last_pos = pos;
                last_time = time;
                alt_last_time = alt_time;

                if (-vel < -1000000 ) {
                    Log.d("AHHH", "lt " + last_time + " time: " + time + " vel " + vel + " lp " + last_pos + " pos " + pos);
                }
            }
            if (log){
                Log.d(TAG, "," + time + "," + pos + "," + vel + "," + alt_v);
            }
        }
    }

    //Radians / s
    public double getVel(){
        //update();
        return vel;
    }

    public void reset(){
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public String toString(){
        return TAG;
    }
}
