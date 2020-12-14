package org.firstinspires.ftc.teamcode.Components.actuators;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Motor extends BaseActuator {
    private double last_power=0;
    private double last_vel = 0;
    double power = 0;
    double velocity = 0;
    enum MOTOR_MODE {POWER, VEL}
    MOTOR_MODE mode = MOTOR_MODE.POWER;
    DcMotorEx internal_motor;
    String TAG = "DCMOTOR ";

    public Motor(String name){
        super();
        internal_motor = Globals.hardwareMap.get(DcMotorEx.class, name);
        internal_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //default zero power behavior should probably be this.
        internal_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TAG = TAG + name + " PORT " + internal_motor.getPortNumber();
    }

    //If not being used for some reason
    public void disableEncoder(){
        internal_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void setZeroPowerMode(DcMotor.RunMode mode){
        internal_motor.setMode(mode);
    }

    public void setpidcoef(double kp, double ki, double kd){
        double kf = 0; //when I learn what this is, I'll do it.
        internal_motor.setVelocityPIDFCoefficients(kp, ki, kd, kf);
    }
    // Radians / second
    public void setVelocity(double val){
        mode = MOTOR_MODE.VEL;
        velocity = val;
    }

    public void setPower(double val){
        mode = MOTOR_MODE.POWER;
        power = val;
    }

    @Override
    public boolean hasChanged() {
        boolean changed;
        if (mode == MOTOR_MODE.POWER){
            changed = power==last_power;
            last_power = power;
        } else {
            changed = velocity == last_vel; //Update every loop timing when controlling velocity.
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
    public void update() {
        if (mode == MOTOR_MODE.POWER) internal_motor.setPower(power);
        else internal_motor.setVelocity(velocity, AngleUnit.RADIANS);
    }
}
