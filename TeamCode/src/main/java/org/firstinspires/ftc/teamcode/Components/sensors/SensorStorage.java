package org.firstinspires.ftc.teamcode.Components.sensors;

import java.util.ArrayList;

public class SensorStorage {
    static ArrayList<BaseSensor> sensors = new ArrayList<>();

    public void addSensor(BaseSensor s){
        sensors.add(s);
    }
}
