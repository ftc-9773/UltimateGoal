package org.firstinspires.ftc.teamcode.actuators;

import org.firstinspires.ftc.teamcode.opmodes.Globals;

public class Servo extends BaseActuator{
    double position;
    double last_pos;
    com.qualcomm.robotcore.hardware.Servo internal_servo;

    public Servo(String name){
        internal_servo = Globals.hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, name);
    }

    public void setPosition(double pos){
        position = pos;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean hasChanged() {
        return false;
    }
}
