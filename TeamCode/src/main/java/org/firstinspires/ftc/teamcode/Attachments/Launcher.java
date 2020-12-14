package org.firstinspires.ftc.teamcode.Attachments;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Utilities.EggTimer;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;

public class Launcher {
    Motor centralMotor;
    Servo flickServo;

    double flickOpenPos, flickClosePos;
    double motorSpeed;

    public Launcher(){
        centralMotor = new Motor("launchMotor");
        flickClosePos = 0;
        flickOpenPos = 0;
    }

    public void motorOn(){centralMotor.setVelocity(motorSpeed);}

    public void motorOff(){centralMotor.setVelocity(0);}

    public void launchDisk(){
        // Wait until motor is at speed, and then launch the disk.
        if (centralMotor.getVelocity() < motorSpeed){
            motorOn();
            new Serialiser() {
                @Override
                public boolean condition() {
                    return centralMotor.getVelocity() < motorSpeed;
                }
                @Override
                public void onConditionMet() {
                    launchDisk();
                }
            }.start();
            return;
        }
        flickServo.setPosition(flickOpenPos);
        new EggTimer(){
            @Override
            public long getLen() {
                return 500;
            }

            @Override
            public void onComplete() {
                flickServo.setPosition(flickClosePos);
            }
        }.start();
    }
}
