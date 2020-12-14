package org.firstinspires.ftc.teamcode.Components.actuators;

import java.util.ArrayList;

/**
 * mostly static class full of actuators
 * */
public class ActuatorStorage {
    static ArrayList<BaseActuator> actuators = new ArrayList<>();

    static void add_actuator(BaseActuator a){
        actuators.add(a);
    }
}
