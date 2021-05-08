package org.firstinspires.ftc.teamcode.Components.actuators;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.android.dex.SizeOf;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;

import java.util.ArrayList;

public class SimpleMotor extends BaseActuator {
    private double last_power=0;
    private double last_vel = 0;
    double minTime = 5;

    double last_pos = 0;
    double last_time = 0;


    double power = 0;
    double velocity = 0;

    enum MOTOR_MODE {POWER, VELOCITY}
    MOTOR_MODE mode = MOTOR_MODE.POWER;

    DcMotor internal_motor;
    DcMotor internal_encoder;
    String TAG = "DCMOTOR ";

    PIDController velocityPID;
    Serialiser velocityCalculator = null;


    public SimpleMotor(String name){
        super();
        internal_motor = Globals.hardwareMap.get(DcMotor.class, name);
        setZeroPowerMode(DcMotor.ZeroPowerBehavior.FLOAT); //default zero power behavior should probably be this.
        velocityPID = new PIDController(1, 0.1, 0);
        TAG = TAG + name + " PORT " + internal_motor.getPortNumber();
    }

    //If not being used for some reason
    public void disableEncoder(){
        internal_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void resetEncoder() {
        internal_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        internal_encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setZeroPowerMode(DcMotor.ZeroPowerBehavior mode){
        internal_motor.setZeroPowerBehavior(mode);
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
        if (System.currentTimeMillis() - last_time < minTime){
            return last_vel;
        }
        double pos = internal_motor.getCurrentPosition();
        double time = System.currentTimeMillis();
        double output = (pos - last_pos) / 28 / (time - last_time);
        last_pos = pos;
        last_time = time;
        return output;
    }

    //WRITE value to hardware
    @Override
    public synchronized void update() {
        if (internal_motor != null) { //if somehow the actuator thread attempts to call this during initialisaiton.
            if (mode == MOTOR_MODE.POWER) internal_motor.setPower(power);
            else {
                //internal_motor.setVelocity(velocity, AngleUnit.RADIANS);
            }
        }
    }

    public String toString(){
        return TAG;
    }

    public int getPosition(){
        return internal_motor.getCurrentPosition();
    }

    public ArrayList<String> getdebuginfo(){
        Log.d(TAG, "requested debug info ");
        ArrayList<String> output = new ArrayList();
        output.add(TAG);
        //output.add("PIDC: " + internal_motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
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
