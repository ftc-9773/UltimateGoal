package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.List;

public abstract class BasicOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //Automatic bulk reads, could maybe be optimised by we probably don't need to.
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        Globals.hardwareMap = hardwareMap;
        Globals.opMode = this;

        run();
    }

    public abstract void run();
}
