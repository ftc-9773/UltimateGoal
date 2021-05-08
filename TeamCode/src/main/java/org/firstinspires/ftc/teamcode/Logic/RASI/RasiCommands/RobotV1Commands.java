package org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands;

import android.provider.Settings;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Logic.DriveUtils;
import org.firstinspires.ftc.teamcode.Utilities.EggTimer;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

import static java.lang.Math.*;


public class RobotV1Commands extends RasiCommands{
    DriveUtils driveUtils;

    public RobotV1Commands(){
         driveUtils = new DriveUtils();
    }

    public void drive(double x, double y){
        driveUtils.pidDrive(x, y);
    }

    //Degrees
    public void rotate(double ang){
        driveUtils.rotateToHeading(ang);
    }

    public void intakeOn(){
        Globals.intake.on();
    }

    public void intakeOff(){
        Globals.intake.off();
    }

    public void lowerMotorSpeed(){
        Globals.launcher.setMode(Launcher.MODE.POLE);
    }

    public void raiseMotorSpeed(){
        Globals.launcher.setMode(Launcher.MODE.BASKET);
    }

    public void setTrackedHeading(double heading){
        driveUtils.setTrackedHeading(heading * Math.PI / 180);
    }

    public void Launch(){
        Globals.launcher.launchDisk();
        waitTime(0.01);
        while (Globals.launcher.getLaunchTaskState() && Globals.opMode.opModeIsActive()){

        }
    }
    public void motorsOn(){
        Globals.launcher.motorOn();
    }

    public void motorsOff(){
        Globals.launcher.motorOff();
    }

    public void stopDrive(){
        Globals.drivebase.driveSimple(0,0,0);
    }

    public void lowerArm(){
        Globals.hook.down();
        waitTime(0.5);
        //Globals.hook.stop();
    }

    public void raiseArm(){
        Globals.hook.up();
        waitTime(0.1);
    }

    public void intakeUp(){
        Globals.intake.up();
    }

    public void hold(){
        Globals.hook.hold();
    }

    public void openHook(){
        Globals.hook.openHook();
    }

    public void closeHook(){
        Globals.hook.closeHook();
    }
}
