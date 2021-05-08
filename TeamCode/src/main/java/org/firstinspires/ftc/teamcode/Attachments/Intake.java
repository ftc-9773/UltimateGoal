package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;

public class Intake {
    public Motor motor;
    public Servo servo;
    double motorSpeed = -1;
    static String TAG = "INTAKE";

    public Intake() {
        motor = new Motor("iMotor");
        servo = new Servo("intakeServo");
        Log.d(TAG, "Created Intake");
    }

    public void up(){
        servo.setPosition(0.75);
    }

    public void down(){
        servo.setPosition(0.25);
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
