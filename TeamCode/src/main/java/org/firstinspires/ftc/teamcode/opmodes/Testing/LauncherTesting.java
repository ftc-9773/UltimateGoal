package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@TeleOp(name = "T_Launcher")
public class LauncherTesting extends BasicOpMode {

    @Override
    public void run() {
        write("Started");
        Launcher launcher = new Launcher();
        write("Created Launcher Successfully");
        waitTime(250);
        write("Turning motors on");
        launcher.motorOn();
        write("Motors on");
        waitTime(1000);
        write("Motors are at correct speed: " + launcher.motorsAtSpeed());
        waitTime(1000);
        write("Turning motors off");
        launcher.motorOff();
        write("Motors off");
        waitTime(1250);
        write("Launching Disk");
        launcher.launchDisk();
        long time = System.currentTimeMillis();
        write("Waiting");
        while (launcher.getLaunchTaskState()){
            write("Waiting for disk launch for " + (System.currentTimeMillis() - time) / 1000 + "seconds");
            if (System.currentTimeMillis() - time > 1500){
                write("Cancelling disk launch");
                launcher.cancelLaunchDisk();
                write("Disk launch cancelled");
            }
        }
        waitTime(1500);
        write("Disk launched");
    }
}
