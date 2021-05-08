package org.firstinspires.ftc.teamcode.Components.sensors;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.Utilities.Button;
import org.firstinspires.ftc.teamcode.Utilities.Globals;

public class LimitSwitch {
    Button state = new Button();
    RevTouchSensor limitSwitch;

    public LimitSwitch(String name){
        limitSwitch = Globals.hardwareMap.get(RevTouchSensor.class, name);
        state.recordNewValue(limitSwitch.isPressed());
    }

    private void read(){
        state.recordNewValue(limitSwitch.isPressed());
    }

    public boolean isJustOn(){
        read();
        return state.isJustOn();
    }

    public boolean isJustOff(){
        read();
        return state.isJustOff();
    }

    public boolean state(){
        read();
        return limitSwitch.isPressed();
    }
}
