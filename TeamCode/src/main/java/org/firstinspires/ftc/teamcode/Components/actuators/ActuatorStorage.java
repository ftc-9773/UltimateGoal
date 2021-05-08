package org.firstinspires.ftc.teamcode.Components.actuators;

import java.util.ArrayList;

/**
 * mostly static class full of actuators
 * */
public class ActuatorStorage {
     static ArrayList<BaseActuator> actuators = new ArrayList<>();

     public static void add_actuator(BaseActuator a){
        synchronized (actuators) {
            actuators.add(a);
        }
    }

     public static void remove_actuator(BaseActuator a){
        synchronized (actuators){
            actuators.remove(a);
        }}

     public static ArrayList<BaseActuator> get_all_actuators(){
        ArrayList<BaseActuator> output = new ArrayList();
        synchronized (actuators){
            output.addAll(actuators);
        }
        return output;
    }
}
