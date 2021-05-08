package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.actuators.Encoder;
import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "Encoder Testing")
public class EncoderTesting extends BasicOpMode {

    @Override
    public void run() {
        Motor motor = new Motor("launchMotor0");
        Motor otherMotor = new Motor("launchMotor1");
        Encoder encoder = new Encoder("launchMotor0");

        while (opModeIsActive()){
            if (gamepad1.left_stick_y > 0.05 || gamepad1.left_stick_y < -0.05){
                motor.setPower(gamepad1.left_stick_y);
                motor.setPower(gamepad1.left_stick_y);
            }
            else{
                motor.setPower(0);
                otherMotor.setPower(0);
            }
//            telemetry.addLine("MV: " + motor.getVelocity());
            telemetry.addLine("MP:" + motor.getPosition());
            telemetry.addLine("V: " + encoder.getVel());
            telemetry.addLine("P: " + encoder.getPos());
            telemetry.update();
        }
    }
}
