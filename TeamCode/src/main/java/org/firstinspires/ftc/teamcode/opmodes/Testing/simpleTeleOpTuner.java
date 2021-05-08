package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Logic.DriveUtils;
import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands.RobotV1Commands;
import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicFullOpMode;

@TeleOp(name = "PID TUNER")
public class simpleTeleOpTuner extends BasicFullOpMode {
    boolean runningRasi;
    RasiInterpreter powerPoleScorer;

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

        Button gp2_X = new Button();
        Button gp2_Y = new Button();
        Button gp2_A = new Button();
        Button gp2_x = new Button();
        Button gp2_RB = new Button();
        Button gp2_LB = new Button();
        Button gp2_B = new Button();
        double adjustfactor = 0.1, adjustadjustfactor = 10;
        boolean adjustingKP = true;

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

            //TUNING PID
            gp2_LB.recordNewValue(gamepad2.left_bumper);
            gp2_RB.recordNewValue(gamepad2.right_bumper);
            gp2_A.recordNewValue(gamepad2.a);
            gp2_B.recordNewValue(gamepad2.b);
            gp2_Y.recordNewValue(gamepad2.y);
            gp2_x.recordNewValue(gamepad2.x);

            if (gp2_x.isJustOn()){
                adjustfactor /=  adjustadjustfactor;
            } else if(gp2_B.isJustOn()){
                adjustfactor *= adjustadjustfactor;
            }
            if (gp2_Y.isJustOn()) {
                if (adjustingKP) {
                    launcher.velPID.KP += adjustfactor;
                } else {
                    launcher.velPID.KD += adjustfactor;
                }
            } else if (gp2_A.isJustOn()){
                if (adjustingKP){
                    launcher.velPID.KP -= adjustfactor;
                } else {
                    launcher.velPID.KD -= adjustfactor;
                }
            }
            if (gp2_LB.isJustOn()){adjustingKP = true;}
            else if (gp2_RB.isJustOn()) adjustingKP = false;

            //TELEMETRY
           telemetry.addLine("Cycle time " + timer.timeElapsed() + " " + rotate());
           telemetry.addLine("Launch Motor speeds " + launcher.getMotorSpeed());
           telemetry.addLine(" ");
           telemetry.addLine("Currently adjusting KP" + adjustingKP);
           telemetry.addLine("Adjust Factor " + adjustfactor);
           telemetry.addLine("KP: " + launcher.velPID.KP);
           telemetry.addLine("KD: " + launcher.velPID.KD);
           //Log.d("PID TRACKING", "" + launcher.getMotorSpeed());
           telemetry.update();

           timer.reset();
        }
    }

    public double abs(double v){
        return v > 0 ? v : -v ;
    }
}
