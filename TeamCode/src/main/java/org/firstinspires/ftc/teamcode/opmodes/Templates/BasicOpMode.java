package org.firstinspires.ftc.teamcode.opmodes.Templates;

import android.util.Log;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Components.actuators.ActuatorManager;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.List;

public abstract class BasicOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //Automatic bulk reads, could maybe be optimised further. I'll probably do that in the sensor manager.
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        Globals.hardwareMap = hardwareMap;
        Globals.opMode = this;
        ActuatorManager manager = new ActuatorManager();
        //manager.start(); //Yeah it's not working and maybe is useless. Will come back to the idea.

        //Put other initialisation code here.
        initialise();
        waitForStart();
        Log.d("BasicOpMode", "Started running opmode");
        run();
        Log.d("BasicOpMode", "Ended opmode");
    }

    public void initialise(){}

    //Implemented by derivative opmodes
    public abstract void run();

    public void telemetry(String msg){
        telemetry.addLine(msg);
    }

    public void write(String msg){
        telemetry(msg);
        telemetry.update();
    }

    public void waitTime(int time){
        long endTime = System.currentTimeMillis() + time;
        while (System.currentTimeMillis() < endTime && opModeIsActive()){

        }
    }
}
