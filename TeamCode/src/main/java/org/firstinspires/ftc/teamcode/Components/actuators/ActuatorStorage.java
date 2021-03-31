package org.firstinspires.ftc.teamcode.Components.actuators;

import java.util.ArrayList;

/**
 * mostly static class full of actuators
 * */
public class ActuatorStorage {
    static ArrayList<BaseActuator> actuators = new ArrayList<>();

    static synchronized void add_actuator(BaseActuator a){
        actuators.add(a);
    }

    static synchronized void remove_actuator(BaseActuator a){ actuators.remove(a); }
}
