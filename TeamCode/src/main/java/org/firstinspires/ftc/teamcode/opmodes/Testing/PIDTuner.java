package org.firstinspires.ftc.teamcode.opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.ViewerParameters;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.sensors.Gyro;
import org.firstinspires.ftc.teamcode.Logic.DriveUtils;
import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Timer;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicOpMode;

@Disabled
@TeleOp(name = "PID Tuner")
public class PIDTuner extends BasicOpMode {

    @Override
    public void run() {
        DcMotor motor = hardwareMap.get(DcMotor.class, "Motor");
        JsonReader reader = new JsonReader("PhysicsJson.json");
        PIDController pid = new PIDController(reader.getDouble("kp"), reader.getDouble("ki"), reader.getDouble("kd"));

    }
}
