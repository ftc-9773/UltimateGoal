package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

public class Intake {
    public Motor motor;
    public Servo servo;
    double motorSpeed = -1;
    static String TAG = "INTAKE";
    double upPos, downPos;

    public Intake() {
        motor = new Motor("iMotor");
        servo = new Servo("intakeServo");
        JsonReader reader = new JsonReader("componentJson");
        upPos = reader.getDouble("inServoUp", 0.5);
        downPos = reader.getDouble("inServoDown", 1.0);
        Log.d(TAG, "Created Intake");
    }

    public void up(){
        servo.setPosition(upPos);
    }

    public void down(){
        servo.setPosition(downPos);
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
