package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Intake;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "i_T")
public class ÍntakeTester extends BasicOpMode {
    Intake intake;

    @Override
    public void initialise() {
        super.initialise();
        intake = new Intake();
    }

    @Override
    public void run() {
        intake.on();

        waitTime(1000);

        intake.off();

        waitTime(1000);

        intake.out();
        waitTime(2000);
        intake.motor.getdebuginfo();
        intake.off();
    }
}
