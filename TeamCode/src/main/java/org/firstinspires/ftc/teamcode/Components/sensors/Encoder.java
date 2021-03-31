package org.firstinspires.ftc.teamcode.Components.sensors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Encoder {
    DcMotorEx encoder;
    String TAG = "ENCODER ";

    public Encoder(String name){
        encoder = Globals.hardwareMap.get(DcMotorEx.class, name);
        TAG = TAG + name + " PORT " + encoder.getPortNumber();
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int getPos(){
        return encoder.getCurrentPosition();
    }

    //Radians / s
    public double getVel(){
        return encoder.getVelocity(AngleUnit.RADIANS);
    }

    public String toString(){
        return TAG;
    }
}
