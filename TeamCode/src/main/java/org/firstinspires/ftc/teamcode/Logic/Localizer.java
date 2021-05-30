package org.firstinspires.ftc.teamcode.Logic;

import org.firstinspires.ftc.teamcode.Components.sensors.Encoder;
import org.firstinspires.ftc.teamcode.Components.sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class Localizer {
    Encoder e0;
    Encoder e1;
    Gyro gyro;

    public Localizer(){
        e0 = new Encoder("");
        e1 = new Encoder("");
        gyro = Globals.gyro;
    }
}
