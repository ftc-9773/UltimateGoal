package org.firstinspires.ftc.teamcode.Attachments;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Components.actuators.Servo;
import org.firstinspires.ftc.teamcode.Components.sensors.LimitSwitch;
import org.firstinspires.ftc.teamcode.Utilities.EggTimer;
import org.firstinspires.ftc.teamcode.Utilities.Serialiser;
import org.firstinspires.ftc.teamcode.Utilities.Threading;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

public class Hook {
    Motor internalMotor;
    LimitSwitch limitSwitch;
    Servo servo;
    double closePos, openPos;
    Threading activeTask = null;
    static final String TAG = "HOOK";

    public Hook(){
        internalMotor = new Motor("hookMotor");
        internalMotor.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);
        limitSwitch = new LimitSwitch("limitSwitch");
        servo = new Servo("hookServo");
        JsonReader reader = new JsonReader("componentJson");
        closePos = reader.getDouble("hookClosePos", 0.1);
        openPos = reader.getDouble("hookOpenPos", 0.5);
    }

    public void down(){
        if (activeTask != null){
            activeTask.interrupt();
            activeTask = null;
            Log.d(TAG, "Going down, stopped active task");
        }
        activeTask = new EggTimer(){
            @Override
            public void during() {
                internalMotor.setPower(0.3);
            }

            @Override
            public void onComplete() {
                internalMotor.setPower(0);

            }
            @Override
            public long getLen() {
                return 600;
            }
        };
        activeTask.start();
    }

    public void hold(){
        if (activeTask != null){
            activeTask.interrupt();
            activeTask = null;
            Log.d(TAG, "Holding, stopped active task");
        }
        activeTask = new Serialiser(true) {
            @Override
            public boolean condition() {
return activeTask == null;
            }

            @Override
            public void onConditionMet() {
                if (limitSwitch.state()){
                    internalMotor.setPower(0);
                } else{
                    internalMotor.setPower(-0.05);
                }
            }
        };
        activeTask.start();
    }

    public void stop(){
        if (activeTask != null){
            activeTask.interrupt();
            activeTask = null;
            Log.d(TAG, "Stopping, stopped active task");
        }
        internalMotor.setPower(0);
    }

    //Run until hits limit switch
    public void up(){
        //closeHook();
        if (activeTask != null){
            activeTask.interrupt();
            activeTask = null;
        }
        activeTask = new Serialiser(){
            @Override
            public boolean condition() {
                return limitSwitch.state();
            }

            @Override
            public void during() {
                internalMotor.setPower(-0.4);
            }

            @Override
            public void onConditionMet() {
                Log.d(TAG, "Arm up, proceeding to hold");
                hold();
            }
        };
        activeTask.start();
    }

    public void closeHook(){
        servo.setPosition(closePos);
    }

    public boolean Switch(){
        return (limitSwitch.state());
    }

    public void openHook(){
        servo.setPosition(openPos);
    }
}
