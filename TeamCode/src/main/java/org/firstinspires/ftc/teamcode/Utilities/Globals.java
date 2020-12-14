package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Globals {
    public static HardwareMap hardwareMap;
    public static LinearOpMode opMode;

    public static double robot_x;
    public static double robot_y;

    public static void update_pos(double x, double y){
        robot_x = x;
        robot_y = y;
    }
}
