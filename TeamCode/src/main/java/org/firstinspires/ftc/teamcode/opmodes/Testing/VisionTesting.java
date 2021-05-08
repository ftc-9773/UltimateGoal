package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicVisionOpMode;

@Disabled
@TeleOp(name = "Vision Testing")
public class VisionTesting extends BasicVisionOpMode {

    @Override
    public void initialise() {
        super.initialise();
        write("Starting TeleOp");
    }

    public boolean isblue(){
        return true;
    }

    @Override
    public void run() {
        Button a = new Button();
        while (opModeIsActive()){
            telemetry.addLine(rotate() + " Vision reading: " + detector.getPosition());
            a.recordNewValue(gamepad1.a);
            if (a.isJustOn()){
                detector.saveLastFrame();
                telemetry.addLine("Saved frame");
            }
            telemetry.update();
        }
    }
}
