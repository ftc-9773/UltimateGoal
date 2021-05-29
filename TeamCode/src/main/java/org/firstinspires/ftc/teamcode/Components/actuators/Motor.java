package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.ArrayList;

public class Motor extends BaseActuator {
    private double last_power=0;
    private double last_vel = 0;

    double power = 0;
    double velocity = 0;

    DcMotor internal_motor;
    //Encoder internal_encoder;
    String TAG = "DCMOTOR ";



    public Motor(String name){
        super();
        internal_motor = Globals.hardwareMap.get(DcMotor.class, name);
        setZeroPowerMode(DcMotor.ZeroPowerBehavior.FLOAT); //default zero power behavior should probably be this.
        //internal_encoder = new Encoder(name);
        //ActuatorStorage.remove_actuator(internal_encoder);
        TAG = TAG + name + " PORT " + internal_motor.getPortNumber();
        internal_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //Needs to be this to use setpower without encoder
    }

    public void setInternal_encoder(Encoder encoder){
        ActuatorStorage.remove_actuator(encoder);
        //internal_encoder = encoder;
    }
    //If not being used for some reason

    //public void resetEncoder() {
        //internal_encoder.reset();
    //}

    //public PIDFCoefficients getPIDFC(){
        //return internal_motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
    //}

    public void _setInternalPID(double k, double i, double d){
        //internal_motor.setVelocityPIDFCoefficients(k,i,d,0);
    }


    //Rad / s
    public void setVelocity(double v){
        //internal_motor.setVelocity(v * Encoder.COUNTS_PER_REVOLUTION_NEVEREST / Math.PI / 2); //In ticks per second. Only used in one spot so....
    }

    //public double getVelocity(){
        //return internal_motor.getVelocity() / Encoder.COUNTS_PER_REVOLUTION_NEVEREST * Math.PI * 2; //In ticks per second
    //}

    public void setZeroPowerMode(DcMotor.ZeroPowerBehavior mode){
        internal_motor.setZeroPowerBehavior(mode);
    }

    public void setPower(double val){
        power = val;
        internal_motor.setPower(power);
        //Log.d(TAG, "set power to " + power);
        //update();
    }

    @Override
    public synchronized boolean hasChanged() {
        return true;
    }

//    public double getVelocity(){
//        return internal_encoder.getVel();
//    }

    //WRITE value to hardware
    @Override
    public synchronized void update() {
//        if (internal_motor != null) { //if somehow the actuator thread attempts to call this during initialisaiton.
//            internal_motor.setPower(power);
//        }
//        if (internal_encoder != null){
//            internal_encoder.update();
//        }
    }

    public String toString(){
        return TAG;
    }

    public long getPosition(){
        return internal_motor.getCurrentPosition();
    }

    public ArrayList<String> getdebuginfo(){
        Log.d(TAG, "requested debug info ");
        ArrayList<String> output = new ArrayList();
        output.add(TAG);
        output.add("CurP: " + power);
        //can add more later;
        StringBuilder sb = new StringBuilder();
        for (String s : output)
        {
            sb.append(s);
            sb.append("; ");
        }
        Log.d(TAG, "returned debug info: " + sb.toString());
        return output;
    }
}
