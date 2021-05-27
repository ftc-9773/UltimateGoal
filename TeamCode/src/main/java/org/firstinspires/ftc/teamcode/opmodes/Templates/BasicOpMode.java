package org.firstinspires.ftc.teamcode.opmodes.Templates;

import android.util.Log;

import com.qualcomm.ftccommon.configuration.EditAnalogInputDevicesActivity;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Components.actuators.ActuatorManager;
import org.firstinspires.ftc.teamcode.Components.actuators.ActuatorStorage;
import org.firstinspires.ftc.teamcode.Components.actuators.BaseActuator;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicOpMode extends LinearOpMode {
    private int state = 0; //For rotating thingy

    @Override
    public void runOpMode() throws InterruptedException {

        //Automatic bulk reads, could maybe be optimised further. I'll probably do that in the sensor manager.
//        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
//        for (LynxModule module : allHubs) {
//            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
//        }


        Globals.hardwareMap = hardwareMap;
        Globals.opMode = this;
        ActuatorManager manager = new ActuatorManager();
        initialise();
        write("Initialised");
        Log.d("BasicOpMode", "Created Actuator Manager");
        write("Adding Actuators");
        Log.d("BasicOpMode", "Adding actuators");
        ArrayList<BaseActuator> actuators = ActuatorStorage.get_all_actuators();
        manager.addActuators(actuators);
        write("Added Actuators"+ actuators.toString() + ", starting manager");
        Log.d("BasicOpMode", "Added Actuators"+ actuators.toString() + ", starting manager");
        Log.d("BasicOpMode", "Starting manager");
        //manager.start();
        write("waiting for start");
        //Put other initialisation code here.
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

    public String rotate(){
        String returnchar = "";
        switch (state) {
            case 0: returnchar = "|"; break;
            case 1: returnchar = "/"; break;
            case 2: returnchar = "-"; break;
            case 3: returnchar = "|"; break;
            case 4: returnchar = "\\";
        }
        state ++;
        if (state > 4) state = 0;
        return returnchar;
    }
}
