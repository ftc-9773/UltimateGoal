package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.ArrayList;

public class Motor extends BaseActuator {
    private double last_power=0;
    private double last_vel = 0;
    double power = 0;
    double velocity = 0;
    enum MOTOR_MODE {POWER, VELOCITY}
    MOTOR_MODE mode = MOTOR_MODE.POWER;
    DcMotorEx internal_motor;
    String TAG = "DCMOTOR ";

    public Motor(String name){
        super();
        internal_motor = Globals.hardwareMap.get(DcMotorEx.class, name);
        setZeroPowerMode(DcMotor.ZeroPowerBehavior.FLOAT); //default zero power behavior should probably be this.
        internal_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TAG = TAG + name + " PORT " + internal_motor.getPortNumber();
    }

    //If not being used for some reason
    public void disableEncoder(){
        internal_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void resetEncoder() {
        internal_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        internal_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setZeroPowerMode(DcMotor.ZeroPowerBehavior mode){
        internal_motor.setZeroPowerBehavior(mode);
    }

    public void setpidcoef(double kp, double ki, double kd){
        double kf = 0; //when I learn what this is, I'll do it.
        internal_motor.setVelocityPIDFCoefficients(kp, ki, kd, kf);
    }
    // Radians / second
    public void setVelocity(double val){
        mode = MOTOR_MODE.VELOCITY;
        velocity = val;
        update();
    }

    public void setPower(double val){
        mode = MOTOR_MODE.POWER;
        power = val;
        update();
    }

    @Override
    public synchronized boolean hasChanged() {
        boolean changed;
        if (mode == MOTOR_MODE.POWER){
            changed = power==last_power;
            last_power = power;
        } else {
            changed = velocity == last_vel;
            last_vel = velocity;
        }
        return changed;
    }

    //Radians / s
    public double getVelocity(){
        return internal_motor.getVelocity(AngleUnit.RADIANS);
    }

    //WRITE value to hardware
    @Override
    public synchronized void update() {
        if (internal_motor != null || true) { //if somehow the actuator thread attempts to call this during initialisaiton.
            if (mode == MOTOR_MODE.POWER) internal_motor.setPower(power);
            else internal_motor.setVelocity(velocity, AngleUnit.RADIANS);
        }
    }

    public String toString(){
        return TAG;
    }

    public ArrayList<String> getdebuginfo(){
        Log.d(TAG, "requested debug info ");
        ArrayList<String> output = new ArrayList();
        output.add(TAG);
        output.add("PIDC: " + internal_motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        output.add("CurV: " + getVelocity());
        output.add("CurP: " + power);
        output.add("Mode: " + mode);
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
