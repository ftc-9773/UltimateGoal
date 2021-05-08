package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "T_Launcher")
public class LauncherTesting extends BasicOpMode {
    Launcher launcher;

    @Override
    public void initialise() {
        super.initialise();
         //launcher = new Launcher();

    }

    @Override
    public void run() {
        write("Started");
        Timer timer = new Timer();
        Button diskLaunch = new Button();
        Button b = new Button();
        Button g1x = new Button();
        Button g1y = new Button();
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "launchMotor0"), motor2 = hardwareMap.get(DcMotor.class, "launchMotor1");
        Button a = new Button();
        Button rightBumper = new Button();
        double motorPowers = 0.1;
        double adjustFactor = 0.01;
        double adjustadjustFactor = 10;
        while (opModeIsActive()){
            b.recordNewValue(gamepad1.b);
            g1x.recordNewValue(gamepad1.x);
            a.recordNewValue(gamepad1.a);
            g1y.recordNewValue(gamepad1.y);
            if (a.isJustOn()){
                motorPowers -= adjustFactor;
            }
            if (b.isJustOn()){
                adjustFactor *= adjustadjustFactor;
            }
            if (g1y.isJustOn()){
                motorPowers += adjustFactor;
            }
            if (g1x.isJustOn()){
                adjustFactor /= adjustadjustFactor;
            }
            if (gamepad1.left_bumper) {
                launcher.flick();
            }
            rightBumper.recordNewValue(gamepad1.right_bumper);
            if (rightBumper.isOn()){
                motor1.setPower(0);
                motor2.setPower(0);
            } else {
                motor1.setPower(motorPowers);
                motor2.setPower(-motorPowers);
            }

            telemetry.addLine(rotate());
            telemetry.addLine("Cycle time " + timer.timeElapsed());
            telemetry.addLine("Launch Motor speeds " + launcher.getMotorSpeed());
            telemetry.addLine("Motor Power " + motorPowers);
            telemetry.addLine("Adjust factor " + adjustFactor);
            timer.reset();
            telemetry.update();
        }
    }
}
