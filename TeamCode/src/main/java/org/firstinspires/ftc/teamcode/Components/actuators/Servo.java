package org.firstinspires.ftc.teamcode.Components.actuators;

import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Servo extends BaseActuator{
    double position;
    double last_pos;
    com.qualcomm.robotcore.hardware.Servo internal_servo;

    public Servo(String name){
        super();
        internal_servo = Globals.hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, name);
    }

    public void setPosition(double pos){
        position = pos;
    }

    @Override
    public void update() {
        internal_servo.setPosition(position);
    }

    @Override
    public boolean hasChanged() {
        return false;
    }
}
