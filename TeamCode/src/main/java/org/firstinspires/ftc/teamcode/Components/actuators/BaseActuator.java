package org.firstinspires.ftc.teamcode.Components.actuators;

/**
 * My idea is basically throwing in the actual update loops into a seperate thread, to hopefully reduce latency
 * this'll basically create a bunch of new motor and servo classes, which abstract the actual updating
 * */
public abstract class BaseActuator{

    public BaseActuator(){
        ActuatorStorage.actuators.add(this);
    }

    public abstract boolean hasChanged();

    public abstract void update();
}
