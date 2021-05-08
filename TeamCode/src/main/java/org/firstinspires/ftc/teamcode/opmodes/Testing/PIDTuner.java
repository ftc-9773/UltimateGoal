package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.ViewerParameters;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.sensors.Gyro;
import org.firstinspires.ftc.teamcode.Logic.DriveUtils;
import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@Disabled
@TeleOp(name = "PID Tuner")
public class PIDTuner extends BasicOpMode {
    MecanumDrivebase drivebase;
    DriveUtils driveUtils;
    double heading;

    @Override
    public void initialise() {
        super.initialise();
        drivebase = new MecanumDrivebase();
        driveUtils = new DriveUtils();
        Globals.gyro = new Gyro();
    }

    @Override
    public void run() {
        write("Starting TeleOp");
        double x, y, r;
        Timer timer = new Timer();
        Button a = new Button();
        Button b = new Button();
        double dkp, dkd, dki;
        Button g1x = new Button();
        Button g1y = new Button();
        JsonReader reader = new JsonReader("auto");
        while (opModeIsActive()){
            x = y = r = 0;
            heading = Globals.gyro.getHeading();
            if (abs(gamepad1.left_stick_x) > 0.07) x = -gamepad1.left_stick_x;
            if (abs(gamepad1.left_stick_y) > 0.07) y = gamepad1.left_stick_y;
            if (abs(gamepad1.right_stick_x) > 0.07) r = -gamepad1.right_stick_x;
            drivebase.driveSimple(x, y, r);
            a.recordNewValue(gamepad1.a);
            b.recordNewValue(gamepad1.b);
            g1x.recordNewValue(gamepad1.x);
            g1y.recordNewValue(gamepad1.y);
            //Test Suite
            if (b.isJustOn()) {
                timer.reset();
                write("Running test suite");
                runTestSuite();
                write("Finshed test suite, time: " + timer.timeElapsed());
            }
            if (a.isJustOn()){
                rebuildPID();
            }
            if (g1x.isJustOn()){
                dkp = gamepad1.left_stick_x;
                dkd = gamepad1.left_stick_y;
                reader.modifyDouble("rkp", dkp);
                reader.modifyDouble("rkd", dkd);
            }
            if (g1y.isJustOn()){
                dki = gamepad1.right_stick_x;
                reader.modifyDouble("rki", dki);
            }

            telemetry.addLine(rotate());
            telemetry.addLine("Cycle time " + timer.timeElapsed());
            timer.reset();
            telemetry.update();
        }
    }
    public double abs(double v){
        return v > 0 ? v : -v ;
    }

    public void rebuildPID(){
        driveUtils = new DriveUtils();
    }

    public void runTestSuite(){
        write("Turning 90 degrees");

    }

}
