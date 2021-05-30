package org.firstinspires.ftc.teamcode.Attachments.Drivebase;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Components.actuators.Motor;
import org.firstinspires.ftc.teamcode.Utilities.Globals;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

/**
 * Mapping relative to the robot: convention is that y axis is front/back and x axis is left/right
 * */
public class MecanumDrivebase {
    Motor mfr, mfl, mbr, mbl;
    enum motorControlModes {VEL, POW} //Experiment for consistency
    motorControlModes mode = motorControlModes.POW;
    double slowx , slowy, slowr;
    public boolean slowMode = false;
    static String TAG = "MDRIVEBASE";
    public boolean fieldCentric = false;
    double fcRotation = 0; //Relative to gyro 0.

    public MecanumDrivebase(){
        mfr = new Motor("frdrive");
        mfr.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);
        mfl = new Motor("fldrive");
        mfl.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);
        mbl = new Motor("bldrive");
        mbl.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);
        mbr = new Motor("brdrive");
        mbr.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);

        JsonReader reader = new JsonReader("componentJson");
        slowx = reader.getDouble("drivebaseXslow", 0.5);
        slowy = reader.getDouble("drivebaseYslow", 0.5);
        slowr = reader.getDouble("drivebaseRslow", 0.5);

    }

    public void setFieldCentricRotation(double r){
        fcRotation = Math.toRadians(r);
    }

    //Drive at Vx, Vy, rotation_speed (units?). Rotates around center of robot.
    public void driveSimple(double x, double y, double r){
        if (slowMode) {
            x *= slowx;
            y *= slowy;
            r *= slowr;
        }

        if (fieldCentric){
            double mag = Math.sqrt(x * x + y * y);
            double ang = Math.atan2(y, x);
            ang -= Globals.gyro.getHeading();
            ang += fcRotation;
            x = mag * Math.cos(ang);
            y = mag * Math.sin(ang);
        }

        if (mode == motorControlModes.POW){
            mfr.setPower(-(y - x - r)); //Motor is pointing the other way
            mfl.setPower(y + x + r);
            mbr.setPower(-(y + x - r)); //Motor is pointing the other way
            mbl.setPower(y - x + r);
        } else if (mode == motorControlModes.VEL){
            mfr.setPower(-(y + x + r));
            mfl.setPower(y - x - r);
            mbr.setPower(-(y - x + r));
            mbl.setPower(y - x - r);
        }
        if (Globals.DEBUG_DRIVEBASE){
            Log.d(TAG, "Drive simple w/ xyr = " + x + "," + y + "," + r + "\n motor powers are: " + (y+x+r) + "," +
                    (y-x-r) + "," + (y+x+r) + "," + (y-x-r));
        }

    }

    public long[] getEncoders(){
        long[] e = new long[4];
        e[0] = mfl.getPosition();
        e[1] = mfr.getPosition();
        e[2] = mbl.getPosition();
        e[3] = mbr.getPosition();
        return  e;
    }

    //INCLUDES THE CENTER AROUND WHICH TO ROTATE
    public void drive(double x, double y, double r){

    }
}
