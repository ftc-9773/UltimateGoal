package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Servo extends BaseActuator{
    double position;
    double last_pos;
    String TAG;
    com.qualcomm.robotcore.hardware.Servo internal_servo;

    public Servo(String name){
        //super();
        internal_servo = Globals.hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, name);
        TAG = "SERVO " + name + " PORT " + internal_servo.getPortNumber();
}

    public void setPosition(double pos){
        Log.d(TAG, "Set position to " + pos);
        position = pos;
        //Log.d(TAG, "Position is now " + position);
        update();
    }

    @Override
    public void update() {
        if (position != last_pos){
            Log.d(TAG, "set new position " + position);
        }
        internal_servo.setPosition(position);
        last_pos = position;
    }

    @Override
    public boolean hasChanged() { return true; }

    public String toString(){
        return TAG;
    }
}
