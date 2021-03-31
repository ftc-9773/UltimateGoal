package org.firstinspires.ftc.teamcode.opmodes.Templates;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Intake;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;

/**
 * OpMode template which automatically makes the entire robot available.
 * */
public abstract class BasicFullOpMode extends BasicOpMode {
    Launcher launcher;
    Intake intake;
    MecanumDrivebase drivebase;
    @Override
    public void initialise() {
        //Create the whole robot system here, so that it's easier to create opmodes which need full robots.
        Launcher launcher = new Launcher();
        Intake intake = new Intake();
        drivebase = new MecanumDrivebase();
    }
}
