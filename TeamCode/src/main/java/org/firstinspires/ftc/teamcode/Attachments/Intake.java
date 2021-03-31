package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;

public class Intake {
    public Motor motor;
    double motorSpeed = -1;
    static String TAG = "INTAKE";

    public Intake() {
        motor = new Motor("iMotor");
        Log.d(TAG, "Created Intake");
    }

    public void on(){
        motor.setPower(motorSpeed);
    }

    public void off(){
        motor.setPower(0);
    }

    public void out(){
        motor.setPower(-motorSpeed);
    }

}
