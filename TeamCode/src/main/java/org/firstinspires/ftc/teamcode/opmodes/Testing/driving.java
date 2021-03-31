package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name="T_Driver")
public class driving extends BasicOpMode {
    MecanumDrivebase drivebase;

    @Override
    public void initialise() {
        drivebase = new MecanumDrivebase();
    }

    @Override
    public void run() {

    }

}
