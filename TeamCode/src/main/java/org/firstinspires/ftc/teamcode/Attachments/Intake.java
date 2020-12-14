package org.firstinspires.ftc.teamcode.Attachments;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;

public class Intake {
    Motor motorTheFirst;
    Motor motorTheSecond;


    public Intake() {
        motorTheFirst = new Motor("iMotor1");
        motorTheSecond = new Motor("iMotor2");
    }
}
