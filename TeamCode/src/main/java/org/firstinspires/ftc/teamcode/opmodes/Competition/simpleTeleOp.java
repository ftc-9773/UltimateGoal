package org.firstinspires.ftc.teamcode.opmodes.Competition;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.actuators.ActuatorStorage;
import org.firstinspires.ftc.teamcode.Components.actuators.BaseActuator;
import org.firstinspires.ftc.teamcode.Logic.DriveUtils;
import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands.RobotV1Commands;
import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicFullOpMode;

@TeleOp(name = "teleOp")
public class simpleTeleOp extends BasicFullOpMode {
    boolean runningRasi;
    RasiInterpreter powerPoleScorer;

    @Override
    public void initialise() {
        super.initialise();
        Globals.restingVoltage = getBatteryVoltage();
    }

    @Override
    public void run() {
        write("Starting TeleOp");
        double x, y, r;
        //launcher.motorOn();
        Timer timer = new Timer();
        Button diskLaunch = new Button();
        Button b = new Button();
        Button g1x = new Button();
        Button g1y = new Button();
        Button a = new Button();
        Button gp2A = new Button();
        Button gp2B = new Button();
        Button gp2Y = new Button();
        Button gp2X = new Button();
        Button hitPowerPoles = new Button();
        boolean holdHook = false;
        boolean hookState = false; //Close
        boolean otherhookstate = false; //Up
        boolean holdHeading = false;
        DriveUtils driveUtils = new DriveUtils();
        double heading = 0;
        powerPoleScorer = new RasiInterpreter("/sdcard/FIRST/ftc9773/2021/rasi/", "powerPole.rasi");
        Serialiser s;
        powerPoleScorer.processRasiCommands(new RobotV1Commands());
        gyro.setZeroPosition();
        while (opModeIsActive()){
            hitPowerPoles.recordNewValue(gamepad2.right_bumper);
            if (hitPowerPoles.isJustOn()){
                runningRasi = true;
                s = new Serialiser() {
                    @Override
                    public boolean condition() {
                        return runningRasi;
                    }
                    @Override
                    public void during(){
                        if (gamepad2.left_bumper){
                            runningRasi = false;
                            powerPoleScorer.interrupt();
                        }
                    }

                    @Override
                    public void onConditionMet() {
                    }
                };
                s.start();
                powerPoleScorer.compileRasi();
                runningRasi = false;
            }
            //DRIVING
            gp2A.recordNewValue(gamepad2.a);
            gp2B.recordNewValue(gamepad1.left_stick_button);
            if (gp2B.isJustOn()){
                heading = gyro.getHeading();
            }
            if (gamepad1.right_stick_button){
                holdHeading = true;
            } else {
                holdHeading = false;
            }
            x = y = r = 0;
            drivebase.slowMode = gamepad1.right_bumper;
            if (abs(gamepad1.left_stick_x) > 0.1) x = -gamepad1.left_stick_x;
            if (abs(gamepad1.left_stick_y) > 0.085) y = gamepad1.left_stick_y;
            if (holdHeading){
                driveUtils.setTrackedHeading(heading);
                driveUtils.maintainHeading(x, y);
            } else {
                if (abs(gamepad1.right_stick_x) > 0.1) r = -gamepad1.right_stick_x;
                drivebase.driveSimple(x, y, r);
            }
            //INTAKE
            gp2Y.recordNewValue(gamepad1.dpad_down);
            gp2X.recordNewValue(gamepad1.dpad_up);
            if (gp2Y.isJustOn()){
                intake.down();
            } else if (gp2X.isJustOn()){
                intake.up();
            }
            diskLaunch.recordNewValue(gamepad1.left_trigger > 0.05);
            if (diskLaunch.isOn()){
                intake.on();
            } else if (abs(gamepad1.right_trigger) > 0.05){
                intake.out();
            } else {
                intake.off();
            }

            //LAUNCHER
           b.recordNewValue(gamepad1.b);
           g1x.recordNewValue(gamepad1.x);
           if (b.isJustOff()) {
               launcher.cancelLaunchDisk();
               launcher.motorOff();
           }
           if (g1x.isOn()) {
               launcher.setMode(Launcher.MODE.POLE);
               launcher.motorOn();
           } else {
               launcher.setMode(Launcher.MODE.BASKET);
               //launcher.motorOn();
           }
            if (gamepad1.left_bumper) {
                launcher.launchDisk();
            }

           //HOOK
            g1y.recordNewValue(gamepad1.y);
            if (g1y.isJustOn()) {
                if (otherhookstate){
                    hook.down();
                    hook.openHook();
                    holdHook = false;
                    otherhookstate = false;
                } else {
                    hook.up();
                    holdHook = true;
                    otherhookstate = true;
                }
            }
            //Open And close hook
            a.recordNewValue(gamepad1.a);
            if (a.isJustOn()){
                if (hookState){
                    hook.openHook();
                } else {
                    hook.closeHook();
                }
                hookState = !hookState;
            }

            if (launcher.getMotorSpeed() == 0 && abs(gamepad1.left_stick_x) > 0.1 && abs(gamepad1.left_stick_y) < 0.1 && abs(gamepad1.right_stick_x) < 0.1 && gamepad1.left_trigger < 0.1 && gamepad1.right_trigger < 0.1){
                Globals.restingVoltage = getBatteryVoltage();
            }
            //TELEMETRY
           telemetry.addLine("Switch state " + hook.Switch());
           telemetry.addLine(rotate());
           telemetry.addLine("Cycle time " + timer.timeElapsed());
           telemetry.addLine("Launch Motor speeds " + launcher.getMotorSpeed());
           telemetry.addLine("Voltage: " + Globals.restingVoltage);
           //telemetry.addLine("Heading: " + gyro.getHeading());
           //Log.d("PID TRACKING", "" + launcher.getMotorSpeed());
           telemetry.update();

           timer.reset();
        }
    }

    public double abs(double v){
        return v > 0 ? v : -v ;
    }
}
