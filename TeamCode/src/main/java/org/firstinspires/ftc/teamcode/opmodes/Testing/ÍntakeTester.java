package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Intake;
import org.firstinspires.ftc.teamcode.Components.actuators.Encoder;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;


@Disabled
@TeleOp(name = "i_T")
public class ÍntakeTester extends BasicOpMode {
    Intake intake;
    Encoder xEncoder, yEncoder;
    MecanumDrivebase drivebase;
    int[] lastEncoders;
    int[] encoders;

    @Override
    public void initialise() {
        super.initialise();
        intake = new Intake();
        xEncoder = new Encoder("hookMotor");
        yEncoder = new Encoder("launchMotor1");
        drivebase = new MecanumDrivebase();
        //lastEncoders = drivebase.getEncoders();

    }

    @Override
    public void run() {
        double x,y,z,r;
        while (opModeIsActive()){
            //encoders = drivebase.getEncoders();
            telemetry.addLine("fl " + encoders[0]);
            telemetry.addLine("fr " + encoders[1]);
            telemetry.addLine("bl " + encoders[2]);
            telemetry.addLine("br " + encoders[3]);
            double dy = (encoders[0] - lastEncoders[0]) + (encoders[2] - lastEncoders[2]) - (encoders[1] - lastEncoders[1]) - (encoders[3] - lastEncoders[3]);
            dy = dy / 4;
            dy = dy / (28 * 20 * 30 / 36) * 4 * Math.PI;
            telemetry.addLine("Y: " + dy);
            double dx = (encoders[0] - lastEncoders[0]) - (encoders[2] - lastEncoders[2]) + (encoders[1] - lastEncoders[1]) - (encoders[3] - lastEncoders[3]);
            dx = dx / 4;
            dx = dx / (28 * 20 * 30 / 36) * 4 * Math.PI ;
            telemetry.addLine("X: " + dx);
            x = y = r = 0;
            if (abs(gamepad1.left_stick_x) > 0.07) x = -gamepad1.left_stick_x;
            if (abs(gamepad1.left_stick_y) > 0.07) y = gamepad1.left_stick_y;
            if (abs(gamepad1.right_stick_x) > 0.07) r = -gamepad1.right_stick_x;
            drivebase.driveSimple(x, y, r);
            telemetry.update();
            if (gamepad1.a){
                yEncoder.encoder.setPower(0.5);
            } else {
                yEncoder.encoder.setPower(0);
            }
        }

    }

    public double abs(double v){
        return v > 0 ? v: - v ;
    }
}
