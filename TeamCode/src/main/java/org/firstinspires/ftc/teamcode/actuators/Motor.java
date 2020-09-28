package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.opmodes.Globals;

import java.util.Queue;

public class Motor extends BaseActuator {
    double last_power=0;
    double power=0;
    DcMotorEx internal_motor;

    public Motor(String name){
        internal_motor = Globals.hardwareMap.get(DcMotorEx.class, name);
    }

    public void setPower(double val){
        power = val;
    }

    @Override
    public boolean hasChanged() {
        boolean changed = power==last_power;
        last_power = power;
        return changed;
    }

    //WRITE value to hardware
    @Override
    public void update() {
        internal_motor.setPower(power);
    }
}
