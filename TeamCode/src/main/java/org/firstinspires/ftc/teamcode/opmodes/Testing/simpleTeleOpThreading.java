package org.firstinspires.ftc.teamcode.opmodes.Testing;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicFullOpMode;

@TeleOp(name = "T_teleOp")
@Disabled
public class simpleTeleOpThreading extends BasicFullOpMode {
    Serialiser s;
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
        while (opModeIsActive()){
            x = y = r = 0;
            if (abs(gamepad1.left_stick_x) > 0.07) x = -gamepad1.left_stick_x;
            if (abs(gamepad1.left_stick_y) > 0.07) y = gamepad1.left_stick_y;
            if (abs(gamepad1.right_stick_x) > 0.07) r = -gamepad1.right_stick_x;
           drivebase.driveSimple(x, y, r);

           b.recordNewValue(gamepad1.b);
           g1x.recordNewValue(gamepad1.x);
           if (b.isJustOff()) {
               launcher.cancelLaunchDisk();
               launcher.motorOff();
           } else if (g1x.isJustOff()) {
               launcher.motorOn();
           }
           //Edge detection
           diskLaunch.recordNewValue(gamepad1.left_trigger > 0.05);
           if (diskLaunch.isOn()){
               launcher.launchDisk();
           }
//           if (diskLaunch.isJustOff()){
//               launcher.cancelLaunchDisk();
//           }

            if (gamepad1.dpad_up) {
                hook.up();
            } else if (gamepad1.dpad_down) {
                hook.down();
            } else {
                hook.stop();
            }
            g1y.recordNewValue(gamepad1.y);
            a.recordNewValue(gamepad1.a);
            if (g1y.isJustOn()){
                hook.closeHook();
            }  else if (a.isJustOn()) {
                hook.openHook();
            }

           if (gamepad1.right_bumper) {
               intake.on();
           } else if (gamepad1.left_bumper) {
               Log.d("TELEOP", "Creating Serialiser");
               s = new Serialiser() {
                   long starttime = System.currentTimeMillis();
                   @Override
                   public boolean condition() {
                       return System.currentTimeMillis() - starttime > 10000;
                   }

                   @Override
                   public void onConditionMet() {
                       Log.d("TELEOP","Condition Met");
                       deleteSerialiser();
                   }
               };
               Log.d("TELEOP", "Starting");
               s.start();
               Log.d("TELEOP", "Started");
           } else {
               intake.off();
           }
           drivebase.slowMode = abs(gamepad1.right_trigger) > 0.05;
           telemetry.addLine(rotate());
           telemetry.addLine("Cycle time" + timer.timeElapsed());
           telemetry.update();
           timer.reset();
        }
    }

    public void deleteSerialiser(){
        s = null;
    }

    public double abs(double v){
        return v > 0 ? v : -v ;
    }
}
