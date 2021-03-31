package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "T_AttachmentSystem")
public class AttachmentSystem extends BasicOpMode {

    @Override
    public void run() {
        Motor motor = new Motor("motor");
        Servo servo = new Servo("servo");

        //waitForStart();
        telemetry(motor.toString());
        telemetry(servo.toString());
        telemetry(motor.getdebuginfo().get(0));
        telemetry(motor.getdebuginfo().get(1));
        telemetry.update();
        waitTime(5000);
        //apears to be about 26 max vel
        motor.setPower(1);
        long time = System.currentTimeMillis() + 4000;
        while (System.currentTimeMillis() < time && !this.isStopRequested()){
            telemetry("Vel: " + motor.getVelocity());
            telemetry.update();
        }
        motor.setPower(0);
        waitTime(250);
        write("moving motor");
        motor.setVelocity(20);
        time = System.currentTimeMillis() + 4000;
        while (System.currentTimeMillis() < time && !this.isStopRequested()){
            telemetry("Vel: " + motor.getVelocity());
            telemetry.update();
        }
        write("stopping motor");
        motor.setVelocity(0);
        waitTime(250);
        telemetry("Motor should be stationary");
        telemetry.update();
        motor.setPower(0);

        servo.setPosition(1);
        waitTime(1000);
        servo.setPosition(0);
        waitTime(1000);
    }
}
