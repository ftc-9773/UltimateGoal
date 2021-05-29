package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;


@TeleOp(name="EncoderTesting")
public class T_thread extends BasicOpMode {
    int eggs_cooked = 0;


    @Override
    public void run() {
        //Encoder encoder = new Encoder("encoderTest");
        Motor motor = new Motor("iMotor");
        while (opModeIsActive()){
//            telemetry.addLine("E:" + encoder.getPos());
//            telemetry.addLine("V:" + encoder.getVel());
//            telemetry.addLine("MV: " + motor.getVelocity());
            telemetry.update();
            if (gamepad1.left_stick_y > 0.1 || gamepad1.left_stick_y < -0.1) motor.setPower(gamepad1.left_stick_y);
            else motor.setPower(0);
        }
    }

}