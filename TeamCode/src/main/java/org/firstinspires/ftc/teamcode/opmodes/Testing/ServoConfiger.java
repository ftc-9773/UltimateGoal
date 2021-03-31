package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "ServoConfig")
public class ServoConfiger extends BasicOpMode {

    @Override
    public void run() {
//        com.qualcomm.robotcore.hardware.Servo s = Globals.hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, "servo");
//        s.setPosition(0);
//        waitTime(5000);
//        s.setPosition(1);
//        waitTime(5000);
        Servo servo = new Servo("servo");
        write("0");
        servo.setPosition(0);
        waitTime(1500);
        write("1");
        servo.setPosition(0.1);
        waitTime(1500);
        write("2");
        servo.setPosition(0.2);
        waitTime(1500);
        write("3");
        servo.setPosition(0.3);
        waitTime(1500);
        write("4");
        servo.setPosition(0.4);
        waitTime(1500);
        write("5");
        servo.setPosition(0.5);
        waitTime(1500);
        write("6");
        servo.setPosition(0.6);
        waitTime(1500);
        write("7");
        servo.setPosition(0.7);
        waitTime(1500);
        write("8");
        servo.setPosition(0.8);
        waitTime(1500);
        write("9");
        servo.setPosition(0.9);
        waitTime(1500);
        write("10");
        servo.setPosition(10);
        waitTime(1500);
    }
}
