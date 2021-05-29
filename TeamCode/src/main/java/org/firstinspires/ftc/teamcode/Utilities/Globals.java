package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Attachments.Drivebase.MecanumDrivebase;
import org.firstinspires.ftc.teamcode.Attachments.Hook;
import org.firstinspires.ftc.teamcode.Attachments.Intake;
import org.firstinspires.ftc.teamcode.Attachments.Launcher;
import org.firstinspires.ftc.teamcode.Components.actuators.ActuatorStorage;
import org.firstinspires.ftc.teamcode.Components.sensors.Gyro;

public class Globals {
    public static HardwareMap hardwareMap;
    public static LinearOpMode opMode;

    public static Gyro gyro;
    public static MecanumDrivebase drivebase;
    public static Launcher launcher;
    public static Intake intake;
    public static Hook hook;

    public static ActuatorStorage storage;

    public static double restingVoltage = 13;

    public static Boolean DEBUG_DRIVEBASE = false;
    public static Boolean DEBUG_LAUNCHER = true;
    public static Boolean DEBUG_ACTUATOR = false;


    public static double robot_x;
    public static double robot_y;

    public static void update_pos(double x, double y){
        robot_x = x;
        robot_y = y;
    }
}
