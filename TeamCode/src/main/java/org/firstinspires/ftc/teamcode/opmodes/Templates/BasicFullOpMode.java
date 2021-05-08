package org.firstinspires.ftc.teamcode.opmodes.Templates;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Hook;
import org.firstinspires.ftc.teamcode.Attachments.Intake;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

/**
 * OpMode template which automatically makes the entire robot available.
 * */
public abstract class BasicFullOpMode extends BasicOpMode {
    protected Launcher launcher;
    protected Intake intake;
    protected MecanumDrivebase drivebase;
    protected Hook hook;
    protected Gyro gyro;

    @Override
    public void initialise() {
        //Create the whole robot system here, so that it's easier to create opmodes which need full robots.
        launcher = new Launcher();
        intake = new Intake();
        drivebase = new MecanumDrivebase();
        hook = new Hook();
        gyro = new Gyro();
        Globals.gyro = gyro;
        Globals.drivebase = drivebase;
        Globals.intake = intake;
        Globals.hook = hook;
        Globals.launcher = launcher;
        Log.d("OpMode", "Initialised Full Robot");
    }
}
